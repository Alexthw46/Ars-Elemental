#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        jdk "jdk-21" // valid options are: "jdk-8", "jdk-16", "jdk-17" or "jdk-21", choose which one you need
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }
        stage('Build') {
            steps {
                echo 'Building'
                sh './gradlew build'
            }
        }
        stage('Publish') {
            steps {
                echo 'Deploying to Maven'
                sh './gradlew publish'
            }
        }
    }
}
