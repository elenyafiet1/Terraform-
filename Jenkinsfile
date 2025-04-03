pipeline {
    agent any

    environment {
        TF_VAR_AWS_ACCESS_KEY = credentials('AKIAXKUH3BR5UBMBUDMV')
        TF_VAR_AWS_SECRET_KEY = credentials('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')
        TF_DIR = 'E:\\eluu'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'main', url: 'https://github.com/elenyafiet1/Terraform-.git'
            }
        }

        stage('Initialize Terraform') {
            steps {
                dir(env.TF_DIR) {
                    bat 'terraform init -no-color'
                }
            }
        }

        stage('Plan Terraform') {
            steps {
                dir(env.TF_DIR) {
                    bat 'terraform plan -var="AWS_ACCESS_KEY=${TF_VAR_AWS_ACCESS_KEY}" -var="AWS_SECRET_KEY=${TF_VAR_AWS_SECRET_KEY}" -out=tfplan -no-color'
                }
            }
        }

        stage('Apply Terraform') {
            steps {
                input message: 'Proceed with Terraform apply?', ok: 'Yes'
                dir(env.TF_DIR) {
                    bat 'terraform apply -auto-approve tfplan -no-color'
                }
            }
        }
    }

    post {
        success {
            echo 'Terraform pipeline executed successfully!'
        }
        failure {
            echo 'Terraform pipeline failed!'
        }
    }
}
