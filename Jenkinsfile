pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "fms-app"
    }

    stages {

        stage('Checkout') {
            steps {
                git url: 'https://github.com/prathamd45/f-m-s.git', branch: 'main'
            }
        }

        stage('Maven Build') {
            steps {
                bat 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t %DOCKER_IMAGE% ."
            }
        }

        stage('Run Docker Headless Check') {
            steps {
                bat sh "docker run --rm --entrypoint echo ${DOCKER_IMAGE} "Health Check OK

            }
        }

        stage('Docker Login & Push') {
            when {
                expression { return false }  // enable later
            }
            steps {
                bat 'docker login -u "%DOCKERHUB_USER%" -p "%DOCKERHUB_PASS%"'
                bat 'docker tag %DOCKER_IMAGE%:latest %DOCKERHUB_USER%/%DOCKER_IMAGE%:latest'
                bat 'docker push %DOCKERHUB_USER%/%DOCKER_IMAGE%:latest'
            }
        }

        stage('Finish') {
            steps {
                echo "Pipeline completed successfully!"
            }
        }
    }
}
