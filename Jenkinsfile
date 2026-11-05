
pipeline {
    agent any

    triggers {
        githubPush()
    }

    tools {
        jdk 'JDK-17'
        maven 'Maven-3.9.9'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '5'))
    }

    environment {
        TOMCAT_CONTAINER = 'tomcat'
        WAR_NAME = 'employee_task.war'
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Check Tools') {
            steps {
                sh '''
                    java -version
                    mvn -version
                    docker --version
                '''
            }
        }

        stage('Build Application') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Verify WAR') {
            steps {
                sh '''
                    ls -lh target/*.war
                    test -n "$(find target -maxdepth 1 -name '*.war' -print -quit)"
                '''
            }
        }

        stage('Deploy to Tomcat 11') {
            steps {
                sh '''
                    WAR_FILE=$(find target -maxdepth 1 -name '*.war' | head -1)

                    docker exec tomcat rm -rf /usr/local/tomcat/webapps/employee_task

                    docker cp "$WAR_FILE" \
                        tomcat:/usr/local/tomcat/webapps/employee_task.war

                    docker restart tomcat
                '''
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    sleep 20
                    docker ps
                    docker logs --tail 50 tomcat
                '''
            }
        }
    }

    post {
        success {
            echo 'Application deployed to Tomcat 11 successfully!'
        }

        failure {
            echo 'Pipeline failed. Please check Console Output.'
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
