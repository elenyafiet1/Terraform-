pipeline {
    agent any
    
    stages {
        stage('Terraform Init') {
            steps {
                bat 'terraform --version'
                dir('terraform') {
                    bat 'terraform init'
                }
            }
        }
        stage('Terraform Plan') {
            steps {
                dir('terraform') {
                    bat 'terraform plan'
                }
            }
        }
    }
}