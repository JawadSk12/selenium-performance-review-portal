pipeline {

    agent any

    parameters {
        choice(
            name: 'DEPLOY_ENV',
            choices: ['staging', 'production'],
            description: 'Select the deployment environment'
        )
    }

    environment {
        APP_NAME = 'performance-review-portal'
    }

    stages {

        stage('Checkout') {
            steps {
                echo '=========================================='
                echo 'STAGE 1: CHECKOUT'
                echo '=========================================='

                checkout scm

                echo 'Source code checkout completed successfully.'
            }
        }

        stage('Build') {
            steps {
                echo '=========================================='
                echo 'STAGE 2: BUILD'
                echo '=========================================='

                bat 'mvn clean compile'

                echo 'Maven build completed successfully.'
            }
        }

        stage('Package') {
            steps {
                echo '=========================================='
                echo 'STAGE 3: PACKAGE'
                echo '=========================================='

                bat 'mvn clean package -DskipTests'

                echo 'WAR package created successfully.'
            }
        }

        stage('Deploy') {
            steps {
                echo '=========================================='
                echo 'STAGE 4: DEPLOY'
                echo '=========================================='

                echo "Application: ${env.APP_NAME}"
                echo "Deployment environment: ${params.DEPLOY_ENV}"

                echo 'Deployment stage reached successfully.'
            }
        }
    }

    post {

        success {
            echo '=========================================='
            echo 'WEEK 8 PIPELINE SUCCESS'
            echo '=========================================='
            echo "Application: ${env.APP_NAME}"
            echo "Environment: ${params.DEPLOY_ENV}"
            echo 'Checkout: SUCCESS'
            echo 'Build: SUCCESS'
            echo 'Package: SUCCESS'
            echo 'Deploy: SUCCESS'
        }

        failure {
            echo '=========================================='
            echo 'WEEK 8 PIPELINE FAILED'
            echo '=========================================='
        }
    }
}