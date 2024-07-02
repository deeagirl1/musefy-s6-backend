# Use the official Python base image with version 3.9
FROM python:3.9
# Set the working directory inside the container
WORKDIR /recommendation-service

# Copy the requirements.txt file to the working directory
COPY requirements.txt .

# Install the project dependencies
RUN pip install --no-cache-dir --upgrade -r requirements.txt

# Copy the rest of the project files to the working directory
COPY . .

EXPOSE 8000

# Start the FastAPI server when the container starts
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000"]
