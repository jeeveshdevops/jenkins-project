
pipeline {

    agent any

    triggers {
        githubPush()
    }

    tools {
        jdk 'JDK17'
        maven 'Maven'
    }

    environment {
        TOMCAT_CONTAINER = 'tomcat'
        WAR_NAME = 'schedule.war'
        DEPLOY_PATH = '/usr/local/tomcat/webapps'
    }

    stages {

        stage('Git Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Maven Clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn -B verify'
            }
        }

        stage('Verify WAR') {
            steps {
                sh '''
                    echo "Checking WAR file"
                    ls -lh target/
                    test -f target/${WAR_NAME}
                    echo "WAR file verified successfully"
                '''
            }
        }

        stage('Check Tomcat Container') {
            steps {
                sh '''
                    docker inspect "$TOMCAT_CONTAINER"
                    docker exec "$TOMCAT_CONTAINER" \
                    test -d "$DEPLOY_PATH"
                '''
            }
        }

        stage('Backup Previous WAR') {
            steps {
                sh '''
                    docker exec "$TOMCAT_CONTAINER" sh -c \
                    'if [ -f /usr/local/tomcat/webapps/schedule.war ]; then
                       cp /usr/local/tomcat/webapps/schedule.war \
                       /usr/local/tomcat/webapps/schedule-backup.war
                     fi'
                '''
            }
        }

        stage('Remove Old Deployment') {
            steps {
                sh '''
                    docker exec "$TOMCAT_CONTAINER" sh -c \
                    'rm -rf /usr/local/tomcat/webapps/schedule \
                    /usr/local/tomcat/webapps/schedule.war'
                '''
            }
        }

        stage('Deploy WAR') {
            steps {
                sh '''
                    docker cp target/${WAR_NAME} \
                    ${TOMCAT_CONTAINER}:${DEPLOY_PATH}/${WAR_NAME}
                '''
            }
        }

        stage('Restart Tomcat') {
            steps {
                sh 'docker restart "$TOMCAT_CONTAINER"'
            }
        }

        stage('Verify Deployment') {
            steps {
                sh '''
                    echo "Waiting for Tomcat deployment..."

                    for i in $(seq 1 30); do

                        if docker exec "$TOMCAT_CONTAINER" \
                        test -d "$DEPLOY_PATH/schedule"; then

                            echo "Deployment successful!"
                            exit 0
                        fi

                        sleep 2
                    done

                    docker logs --tail 100 "$TOMCAT_CONTAINER"
                    exit 1
                '''
            }
        }
    }

    post {
        success {
            echo 'SUCCESS: GitHub to Jenkins to Tomcat deployment completed!'
        }

        failure {
            echo 'FAILED: Check Jenkins Console Output.'
        }

        always {
            echo 'Pipeline execution completed.'
        }
    }
}
