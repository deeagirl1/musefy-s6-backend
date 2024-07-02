
const { Storage } = require('@google-cloud/storage');
const keyFilename = 'service-account.json';
const { parse } = require('json2csv');
const storage = new Storage({ keyFilename });
const axios = require('axios');


const USER_URL = 'http://localhost:8082';
const MUSIC_URL = 'http://localhost:8087';

exports.uploadDataToGCP = async (req, res) => {
  try {
      // Fetch all user data from your backend
      const users = await getAllUsers();

      // Loop through each user to upload their data
      for (const user of users) {
          const userId = user.id;

          // Fetch user interactions and playlists
          const interactions = await getUserInteractions(userId);
          const playlists = await getUserPlaylists(userId);

          // Create CSV content
          const csvContent = createCSVContent(user, interactions, playlists);

          // Upload the CSV content to the bucket
          const fileName = `user_${userId}/user_data_${userId}.csv`;
          const file = storage.bucket('musefy-export-bucket').file(fileName);
          await file.save(csvContent, { contentType: 'text/csv' });
      }

      res.status(200).send('User data uploaded to GCP successfully');
  } catch (error) {
      res.status(500).send('Error uploading user data to GCP');
  }
};

async function getAllUsers() {
  const response = await axios.get(`${USER_URL}/api/users/`);
  return response.data;
}

async function getUserInteractions(userId) {
  const response = await axios.get(`${MUSIC_URL}/api/songs/interactions/${userId}`);
  return response.data;
}

async function getUserPlaylists(userId) {
  const response = await axios.get(`${MUSIC_URL}/api/playlists/${userId}/playlists`);
  return response.data;
}

function createCSVContent(user, interactions, playlists) {
  const csvUser = parse(user, { fields: Object.keys(user[0] || {}) });

  const csvInteractions = parse(interactions, { fields: Object.keys(interactions[0] || {}) });

  const csvPlaylists = parse(playlists, { fields: Object.keys(playlists[0] || {}) });

  return `${csvUser}\n\n${csvInteractions}\n\n${csvPlaylists}`;
}



// DOWNLOAD USER DATA 
exports.exportUserDataFromGCP = async (req, res) => {
  try {
    const userId = req.query.userId; // Assuming the user ID is provided as a query parameter
    const folderName = `user_${userId}`;
    const bucketName = 'musefy-export-bucket';
    const bucket = storage.bucket(bucketName);

    const [files] = await bucket.getFiles({ prefix: `${folderName}/` });

    if (files.length > 0) {
      const zipFile = await createZipArchive(files);

      res.set({
        'Content-Type': 'application/zip',
        'Content-Disposition': `attachment; filename=${folderName}.zip`,
      });

      zipFile.pipe(res);
      zipFile.finalize();
    } else {
      res.status(404).send('No files found for the user');
    }
  } catch (error) {
    console.error('Error exporting user data from GCP:', error);
    res.status(500).send('An error occurred during data export');
  }
};

function createZipArchive(files) {
  const archiver = require('archiver');
  const zipFile = archiver('zip');

  files.forEach((file) => {
    const fileName = file.name;
    zipFile.file(file.createReadStream(), { name: fileName });
  });

  return zipFile;
}
