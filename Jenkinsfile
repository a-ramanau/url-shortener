pipeline {
  agent any
  stages {
    stage('test stage') {
      steps {
        echo 'hello world'
      }
    }

    stage('build stage') {
      steps {
        sh 'echo building...'
      }
    }

  }
  environment {
    TEST = 'hello'
  }
}