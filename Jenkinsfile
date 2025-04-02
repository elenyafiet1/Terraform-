pipeline {
    agent any
    stages {
        stage('Terraform Plan') {
            steps {
                dir('terraform') {
                    bat 'terraform plan'
                }
            }
        }
    }
}
