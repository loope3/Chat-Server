FROM gradle:jdk17-alpine

# Setup Directory Structure
WORKDIR /home

# Install necessary dependencies
RUN apk add supervisor

# Copy supervisor config file
COPY supervisord.conf /etc/supervisord.conf

# File setup for Back End of Chat Application
WORKDIR /home/back
COPY . /home/back
EXPOSE 8080

# Kick off supervisor to startup Front and Back End
WORKDIR /home
CMD ["/usr/bin/supervisord","-c","/etc/supervisord.conf"]
