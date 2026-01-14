pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "fms-app"
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/<your-username>/<your-repo>.git'
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
                sh 'docker run --rm ${DOCKER_IMAGE}'
            }
        }

        // Optional: push to Docker Hub
        stage('Docker Login & Push') {
            when {
                expression { return false } // enable later if needed
            }
            steps {
                sh 'docker login -u "${DOCKERHUB_USER}" -p "${DOCKERHUB_PASS}"'
                sh 'docker tag ${DOCKER_IMAGE}:latest ${DOCKERHUB_USER}/${DOCKER_IMAGE}:latest'
                sh 'docker push ${DOCKERHUB_USER}/${DOCKER_IMAGE}:latest'
            }
        }

        // Deploy to Kubernetes (Minikube)
        stage('Kubernetes Deploy') {
            steps {
                sh 'kubectl apply -f k8s/deployment.yaml'
                sh 'kubectl apply -f k8s/service.yaml'
            }
        }
    }
}
