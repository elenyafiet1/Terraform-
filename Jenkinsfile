pipeline {
    agent any
    stages {
        stage('Terraform Plan') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-terraform-creds',
                    accessKeyVariable: 'AKIAXKUH3BR5ZAS3J547',
                    secretKeyVariable: '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'
                ]]) {
                    dir('terraform') {
                        bat 'terraform plan'
                    }
                }
            }
        }
    }
}
