FROM docker:latest

# Install dependencies
RUN apk update && apk add --no-cache \
    openjdk8 \
    curl \
    python3 \
    py3-pip

# Set Java environment variables
ENV JAVA_HOME=/usr/lib/jvm/java-1.8-openjdk
ENV PATH=$PATH:$JAVA_HOME/bin

# Install Gradle
ENV GRADLE_VERSION=8.0
RUN curl -L https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip -o gradle-${GRADLE_VERSION}-bin.zip \
    && unzip gradle-${GRADLE_VERSION}-bin.zip -d /opt \
    && mv /opt/gradle-${GRADLE_VERSION} /opt/gradle \
    && rm gradle-${GRADLE_VERSION}-bin.zip

# Set PATH environment variable
ENV PATH=/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin:/opt/gradle/bin

# Install pytest
RUN pip3 install --no-cache-dir pytest

# Set working directory
WORKDIR /app

# Copy the project files to the container
COPY . .

# Define the command to run the tests
CMD ["pytest"]
