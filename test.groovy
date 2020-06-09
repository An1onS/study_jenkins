pipeline {
    agent{node('master')}
    stages {
    stage ('Download from git'){
        checkout([$class: 'GitSCM',
                    branches: [[name: '*/master']],
                    doGenerateSubmoduleConfigurations: false,
                    extensions: [[$class: 'RelativeTargetDirectory',
                    relativeTargetDir: 'pipee2']],
                    submoduleCfg: [],
                    userRemoteConfigs: [[credentialsId: 'IlyaBurakGit',url: 'https://github.com/An1onS/study_jenkins.git']]])           
    }
    stage ('Run Docker'){    
        sh 'docker build -t pipee2 -f Dockerfile .'
        sh 'docker run -itd --name pipee2container pipee2'
        sh 'docker exec pipee2container df -h > dfh.txt'
        sh 'docker exec pipee2container grep cpu /proc/stat > cpu.txt'
    }
    
}
