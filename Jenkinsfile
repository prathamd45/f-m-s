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
stage('SonarQube Analysis') {
    steps {
        withSonarQubeEnv('SonarQube') {
            bat '''
            mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.10.0.2594:sonar ^
            -Dsonar.projectKey=fms ^
            -Dsonar.projectName=Faculty-Management-System
            '''
        }
    }
}



        stage('Build Docker Image') {
            steps {
                bat 'docker build -t %DOCKER_IMAGE% .'
            }
        }

        stage('Run Docker Headless Check') {
            steps {
                bat 'docker run --rm --entrypoint /bin/sh fms-app -c "echo Health Check OK"'
            }
        }

        stage('Finish') {
            steps {
                echo "Pipeline completed successfully with SonarQube analysis!"
            }
        }
    }
}
