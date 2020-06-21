pipeline {
    agent{node('master')}
    stages {
        stage('Dowload project from github') {
            steps {
                script {
                    cleanWs()                    
                }
                script {
                    echo 'Downloading...'
                    checkout([$class                           : 'GitSCM',
                              branches                         : [[name: '*/master']],
                              doGenerateSubmoduleConfigurations: false,
                              extensions                       : [[$class           : 'RelativeTargetDirectory',
                                                                   relativeTargetDir: 'pipee2']],
                              submoduleCfg                     : [],
                              userRemoteConfigs                : [[credentialsId: 'IlyaBurakGit', url: 'https://github.com/An1onS/study_jenkins.git']]])
                }
            }
        }
        stage ('Stop & rm container'){
            steps{
                script{
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'srv_sudo',
                            usernameVariable: 'username',
                            passwordVariable: 'password'
                        )
                    ]){
                        try{
                            sh "echo '${password}' | sudo -S docker stop pipee2container"
                            sh "echo '${password}' | sudo -S docker rm pipee2container"
                        } 
                        catch (Exception e) {
                            print 'No such container, skip cleanup'
                        }                        
                    }
                }
            }
        }
        stage ('Build & Run'){
            steps{
                script{
                    withCredentials([
                        usernamePassword(
                            credentialsId: 'srv_sudo',
                            usernameVariable: 'username',
                            passwordVariable: 'password')
                    ]){
                        sh "cp /var/lib/jenkins/workspace/pipee2/pipee2/Dockerfile /home/adminci/study_ansible/SakharovAY/Dockerfile"
                    
                        sh "echo '${password}' | sudo -S docker build /home/adminci/study_ansible/SakharovAY/ -t pipee2"
                        sh "echo '${password}' | sudo -S docker run -itd --name pipee2container -v /home/adminci/study_ansible/SakharovAY/out:/out pipee2"
                        sh "echo '${password}' | sudo -S docker exec pipee2container sh -c 'df -h > /out/dfh.txt'"
                        sh "echo '${password}' | sudo -S docker exec pipee2container sh -c 'grep cpu /proc/stat > /out/cpu.txt'"
                    }                    
                }
            }
        }        
    }    
}
