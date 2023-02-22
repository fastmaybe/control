// Jenkins 节点
node("slavegjsaas") {

    def image
    //def tag

    stage('MAVEN BUILD') {
        def scmVars = checkout scm
        echo "scmVars value: ${scmVars}"
        tag = scmVars.GIT_BRANCH.substring(scmVars.GIT_BRANCH.lastIndexOf("/")+1)+'.'+Calendar.getInstance().getTime().format('YYYYMMddHHmmss')
        if(deploy_version) {
            tag = params.env + '_' + deploy_version.trim()
        } else {
            tag = params.env + '_' + tag
        }
        sh "echo tag : ${tag}"
        sh "mvn clean package -P${params.env} -U -Dmaven.test.skip=true"
    }

    stage('DOCKER BUILD') {
        // docker login
        sh "docker login --username=100008056529 --password='SAAS_docker!@#' ccr.ccs.tencentyun.com"
        // 删除 Jenkins 节点的全部没用的镜像
        sh "docker system prune --force"

        image = docker.build("${params.registry}/${params.system}-${service}:${tag}", "-f ./deploy/gcp/Dockerfile .")
        // 上传镜像
        image.push()
        // 删除镜像
        sh "docker image rm -f ${image.id}"
    }

    stage('DOCKER PUSH') {
        def remote = [:]
        remote.allowAnyHosts = true
        def match = false
        //以下根据项目按需配置即可
        if('prod'=="${params.env}"){
            if ('ibu-sfes-core' == "${params.system}") {
                remote.name = '10.148.32.62'
                remote.host = '10.148.32.62'
                match = true
            }
        } else {
            if ('ibu-sfes-core' == "${params.system}") {
                remote.name = '10.148.241.5'
                remote.host = '10.148.241.5'
                match = true
            }
        }
        if(match){
            withCredentials([usernamePassword(credentialsId: '5134fd1d-a5ce-449d-b400-13e730a1ba1d', passwordVariable: 'password', usernameVariable: 'user')]) {
                remote.user = user
                remote.password = password

                sshCommand remote: remote, command: "~/push.sh ${params.system}-${service}:${tag}"
            }
        }
    }
}