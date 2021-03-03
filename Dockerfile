FROM ubuntu
RUN apt update
RUN apt install -y wget \
curl \
unzip


#Install Maven
RUN apt -y install maven \
   && rm -rf /var/lib/apt/lists/*
RUN apt-get update


#Install OpenJDK 15
RUN apt-get -y install software-properties-common
RUN add-apt-repository ppa:openjdk-r/ppa
RUN apt-get update
RUN apt-get -y install openjdk-15-jdk\
   && rm -rf /var/lib/apt/lists/*


# Install Google Chrome
RUN wget -q -O - https://dl-ssl.google.com/linux/linux_signing_key.pub | apt-key add -
RUN sh -c 'echo "deb [arch=amd64] http://dl.google.com/linux/chrome/deb/ stable main" >> /etc/apt/sources.list.d/google-chrome.list'
RUN apt-get -y update
RUN apt-get install -y google-chrome-stable \
--no-install-recommends \
   && rm -rf /var/lib/apt/lists/*


# Install chromedriver
RUN mkdir -p /app/bin
RUN apt-get install -yqq unzip
RUN wget -O /tmp/chromedriver.zip http://chromedriver.storage.googleapis.com/`curl -sS chromedriver.storage.googleapis.com/LATEST_RELEASE`/chromedriver_linux64.zip
RUN unzip /tmp/chromedriver.zip -d /app/bin/ \
    && rm /tmp/chromedriver.zip \
    && rm -rf /var/lib/apt/lists/*
