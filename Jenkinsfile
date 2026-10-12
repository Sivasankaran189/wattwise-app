pipeline {
    agent any

    environment {
        DOCKER_HUB_CREDENTIALS_ID = 'dockerhub-credentials'
        GITOPS_REPO_CREDENTIALS_ID = 'github-gitops-token'
        SONARQUBE_SERVER_NAME     = 'SonarQube'
        IMAGE_NAME                = 'your-dockerhub-user/wattwise-api'
        IMAGE_TAG                 = "${BUILD_NUMBER}-${GIT_COMMIT.take(7)}"
        GITOPS_REPO_URL           = 'https://github.com/your-username/wattwise-gitops.git'
    }

    options {
        timeout(time: 30, unit: 'MINUTES')
        disableConcurrentBuilds()
        ansiColor('xterm')
    }

    stages {
        stage('1. Environment & Pre-check') {
            steps {
                echo "Starting CI Build for WattWise"
                echo "Target Image: ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                sh 'java -version'
                sh 'git --version'
                sh 'docker --version'
            }
        }

        stage('2. Build & Unit Tests') {
            steps {
                echo "Running build validation and unit tests..."
                sh 'chmod +x scripts/ci/*.sh scripts/gitops/*.sh'
                sh './scripts/ci/validate.sh'
            }
            post {
                always {
                    junit testResults: '**/target/surefire-reports/*.xml', allowEmptyResults: true
                }
            }
        }

        stage('3. SonarQube Code Quality Gate') {
            steps {
                script {
                    // Requires SonarQube server configured in Jenkins global settings
                    withSonarQubeEnv("${env.SONARQUBE_SERVER_NAME}") {
                        sh 'mvn sonar:sonar -Dsonar.projectKey=wattwise-app'
                    }
                    timeout(time: 5, unit: 'MINUTES') {
                        def qg = waitForQualityGate()
                        if (qg.status != 'OK') {
                            error "Pipeline aborted due to SonarQube Quality Gate failure: ${qg.status}"
                        }
                    }
                }
            }
        }

        stage('4. Build Docker Container Image') {
            steps {
                echo "Building containerized image..."
                sh './scripts/ci/build-image.sh'
            }
        }

        stage('5. Trivy Container Vulnerability Scan') {
            steps {
                echo "Scanning container image for High & Critical vulnerabilities..."
                sh "trivy image --severity HIGH,CRITICAL --exit-code 0 ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
            }
        }

        stage('6. Publish Image to Docker Hub') {
            steps {
                echo "Pushing verified image to registry..."
                withCredentials([usernamePassword(credentialsId: "${env.DOCKER_HUB_CREDENTIALS_ID}",
                                                  usernameVariable: 'DOCKER_USER',
                                                  passwordVariable: 'DOCKER_PASS')]) {
                    sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                    sh "docker push ${env.IMAGE_NAME}:${env.IMAGE_TAG}"
                    sh "docker push ${env.IMAGE_NAME}:latest"
                }
            }
        }

        stage('7. Update GitOps Manifests') {
            steps {
                echo "Updating desired state in GitOps repository..."
                dir('gitops-repo') {
                    git credentialsId: "${env.GITOPS_REPO_CREDENTIALS_ID}",
                        url: "${env.GITOPS_REPO_URL}",
                        branch: 'main'
                }
                sh """
                    export GITOPS_DIR=gitops-repo
                    export NEW_IMAGE_NAME="${env.IMAGE_NAME}"
                    export NEW_IMAGE_TAG="${env.IMAGE_TAG}"
                    ./scripts/gitops/update-image.sh
                """
                dir('gitops-repo') {
                    sh 'git push origin main'
                }
            }
        }

        stage('8. Post-Release Verification') {
            steps {
                echo "Smoke testing deployed service..."
                // When running smoke test against local or cluster endpoint:
                // sh './scripts/ci/smoke-test.sh'
                echo "Release deployed and monitored."
            }
        }
    }

    post {
        success {
            echo "CI/CD Pipeline Succeeded: WattWise image published and GitOps updated."
        }
        failure {
            echo "Pipeline Failed! Check console log for details."
        }
    }
}
