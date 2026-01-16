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
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t ${DOCKER_IMAGE} .'
            }
        }

        stage('Run Docker Headless Check') {
            steps {
                sh 'docker run --rm -e RUN_MODE=docker ${DOCKER_IMAGE}'
            }
        }

        stage('Docker Login & Push') {
            when {
                expression { return false }  // enable later
            }
            steps {
                sh 'docker login -u "${DOCKERHUB_USER}" -p "${DOCKERHUB_PASS}"'
                sh 'docker tag ${DOCKER_IMAGE}:latest ${DOCKERHUB_USER}/${DOCKER_IMAGE}:latest'
                sh 'docker push ${DOCKERHUB_USER}/${DOCKER_IMAGE}:latest'
            }
        }

        stage('Finish') {
            steps {
                echo "Pipeline completed successfully!"
            }
        }
    }
}
