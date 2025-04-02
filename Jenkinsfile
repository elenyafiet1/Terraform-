pipeline {
    agent any
    stages {
        stage('Terraform Plan') {
            steps {
                withCredentials([[
                    $class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'aws-terraform-creds', // Must match Jenkins credential ID
                    accessKeyVariable: 'AKIAXKUH3BR5ZAS3J547',
                    secretKeyVariable: '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'
                ]]) {
                    sh 'terraform plan'
                }
            }
        }
    }
}
