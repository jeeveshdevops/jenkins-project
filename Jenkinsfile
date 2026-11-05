cat > Jenkinsfile <<'EOF'
pipeline {
    agent any

    tools {
        jdk 'JDK-17'
        maven 'Maven-3.9.9'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean verify'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh 'docker build -t employee-app:latest .'
            }
        }

        stage('Deploy Application') {
            steps {
                sh '''
                    docker rm -f employee-app || true
                    docker run -d \
                        --name employee-app \
                        -p 8081:8080 \
                        --restart unless-stopped \
                        employee-app:latest
                '''
            }
        }
    }

    post {
        success {
            echo 'Application deployed successfully!'
        }
        failure {
            echo 'Pipeline failed. Check Console Output.'
        }
    }
}
EOF
