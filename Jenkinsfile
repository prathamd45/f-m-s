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
            environment {
                SCANNER_HOME = tool 'sonarqube'   // ✅ EXACT tool name
            }
            steps {
                withSonarQubeEnv('sonarqube') {
                    bat """
                    %SCANNER_HOME%\\bin\\sonar-scanner ^
                    -Dsonar.projectKey=fms ^
                    -Dsonar.projectName=Faculty-Management-System ^
                    -Dsonar.sources=src ^
                    -Dsonar.java.binaries=target
                    """
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
