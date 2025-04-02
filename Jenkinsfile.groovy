pipeline {
    agent any

    environment {
        AWS_ACCESS_KEY_ID     = credentials('aws-access-key')
        AWS_SECRET_ACCESS_KEY  = credentials('aws-secret-key')
        TF_VAR_environment    = 'dev'
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
            }
        }

        stage('Terraform Init') {
            steps {
                dir('terraform') {
                    sh 'terraform init -input=false'
                }
            }
        }

        stage('Terraform Plan') {
            steps {
                dir('terraform') {
                    sh 'terraform plan -out=tfplan'
                }
            }
        }

        stage('Approval') {
            steps {
                script {
                    timeout(time: 1, unit: 'HOURS') {
                        input message: 'Apply Terraform changes?', ok: 'Deploy'
                    }
                }
            }
        }

        stage('Terraform Apply') {
            steps {
                dir('terraform') {
                    sh 'terraform apply -auto-approve tfplan'
                }
            }
        }
    }

    post {
        always {
            cleanWs()  // Clean workspace
        }
        success {
            slackSend channel: '#devops', message: "Terraform deployment succeeded!"
        }
        failure {
            slackSend channel: '#devops', message: "Terraform deployment failed!"
        }
    }
}