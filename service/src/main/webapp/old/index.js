Vue.use(ElementUI);

function trimString(s) {
    if (typeof(x) == "undefined" || x == null) {
        return ""
    }

    return x.replace(/^\s+|\s+$/gm,'');
}

function isSpaceString(x) {
    return trimString(x.toString()) == "";
}

function formatUnknownLog(log) {
    var seq = log.header.sequence;
    var time = log.header.time;

    return [`${seq} ${new Date(time).toLocaleString()} UNKNOWN LOG`];
}

function formatDeviceAndAppInfoLog(log) {
    var seq = log.header.sequence;
    var time = log.header.time;

    var deviceVersion = log.body.deviceVersion;
    var baseOsName = log.body.baseOsName;
    var baseOsVersion = log.body.baseOsVersion;
    var osName = log.body.osName;
    var osVersion = log.body.osVersion;
    var appVersion = log.body.appVersion;

    var part = [
        `${seq} ${new Date(time).toLocaleString()}`,
        `device version: ${deviceVersion}`,
        `base os: ${baseOsName}:${baseOsVersion}`,
        `os: ${osName}:${osVersion}`,
        `app version: ${appVersion}`
    ];
    
    var moduleInfoArray = log.body.moduleInfo;
    for (var i in moduleInfoArray) {
        var name = moduleInfoArray[i].moduleName;
        var version = moduleInfoArray[i].moduleVersion;
        part.push(`module: ${name}:${version}`);
    }

    return part.filter(isSpaceString);
}

function formatMethodAndObjectInfoLog(log) {
    var seq = log.header.sequence;
    var time = log.header.time;

    var method = log.body.method;
    var variable = log.body.variable;

    var sourceFile = method.sourceFile;
    var lineNumber = method.lineNumber;
    var className = method.className;
    var methodName = method.methodName;
    
    var part = [
        `${seq} ${new Date(time).toLocaleString()}`,
        `${sourceFile}:${lineNumber} ${className}.${methodName}`
    ];

    for (var i in variable) {
        var type = variable[i].objectType;
        var value = variable[i].objectValue;
        part.push(`${type}: ${value}`);
    }
    
    return part.filter(isSpaceString);
}

function formatExceptionInfoLog(log) {
    var seq = log.header.sequence;
    var time = log.header.time;

    var error = log.body.exception;
    var stack = log.body.stack;

    var part = [
        `${seq} ${new Date(time).toLocaleString()} EXCEPTION: ${error.objectType}: ${error.objectValue}`
    ];

    for (var i in stack) {
        var sourceFile = stack[i].sourceFile;
        var lineNumber = stack[i].lineNumber;
        var className = stack[i].className;
        var methodName = stack[i].methodName;

        part.push(`${sourceFile}:${lineNumber} ${className}.${methodName}`);
    }

    return part;
}

function formatUserDefinedLog(log) {
    var seq = log.header.sequence;
    var time = log.header.time;

    var sourceFile = log.body.sourceFile;
    var lineNumber = log.body.lineNumber;
    var packageName = log.body.packageName;
    var className = log.body.className;
    var methodName = log.body.methodName;
    var userDefinedMessage = log.body.userDefinedMessage;
    
    var part = [
        `${seq} ${new Date(time).toLocaleString()}`,
        `${sourceFile}:${lineNumber} ${className}.${methodName}`,
        userDefinedMessage
    ].filter(isSpaceString);

    return part;
}

function formatLog(log) {
    var format = [
        formatUnknownLog,
        formatDeviceAndAppInfoLog,
        formatMethodAndObjectInfoLog,
        formatExceptionInfoLog,
        formatUserDefinedLog        
    ];

    var type = log.header.logType;
    if (type >= format.length) {
        type = 0;
    }
    
    return format[type](log);
}

Vue.component("log-viewer", {
    data: function() {
        return this.init();
    },
    methods: {
        init: function() {
            return {
                lines: formatLog(this.log)
            };
        }
    },
    props: ["log"],
    template: `
<div class=log-viewer v-if="lines && lines.length > 0">
  <ol class="nomargin nopadding">
    <li v-for="line in lines" class="message-line">{{line}}</li>
  </ol>
</div>
`
});

var deviceApp = new Vue({
    el: "#device",
    data: {
        show: false,
        deviceIdList: [],
        logList: [],
    },
    methods: {
        run: function() {
            this.show = true;
            this.queryDevice();
        },
        queryDevice: function() {
            axios.get("/api/devices").then(payload => this.deviceIdList = payload.data).catch(error => console.log(error));
        },
        queryDeviceLog: function(deviceId) {
            axios.get("/api/log/" + deviceId).then(payload => this.logList = payload.data).catch(error => console.log(error));
        },
    },
    template: `
<div v-if="show" class="app">
  <div class="left first-column">
    <span class="title">online device</span><br/>
    <span v-if="deviceIdList.length == 0" class="title">NO ONLINE DEVICE</span>
    <ul class="nomarigin nopadding">
      <li v-for="deviceId in deviceIdList" v-on:click="queryDeviceLog(deviceId)" class="device-item">
        {{deviceId.substr(0, 8)}} 
      </li>
    </ul>
  </div>
  <div class="second-column">
    <span class="title">device log</span><br/>
    <span v-if="logList.length == 0" class="title">NO DEVICE LOG</span>
    <ul class="nomarigin nopadding log-list">
      <li v-for="log in logList" class="log-item"><log-viewer v-bind:log=log /></li>
    </ul>
  </div>
</div>
`
});

var logApp = new Vue({
    el: "#log",
    data: {
        show: false,
        deviceId: "",
        logList: [],
        packageName: "",
    },
    methods: {
        run: function() {
            this.show = true;
        },
        query: function() {
            axios.get("/api/log/" + this.deviceId).then(x => {
                console.log(x);
                this.logList = x.data;
            }).catch(e => console.log(e));
        }
    },
    template: `
<div class="app" v-if="show">
  <div>
    <span>device id</span>
    <input v-model="deviceId"></input>
  </div>
  <div>
    <span>package name</span>
    <input v-model="packageName"></input>
  </div>
  <button v-on:click="query()">query</button>
  
  <div>
    <ul class="nomargin nopadding">
      <li v-for="log in logList" class="log-item"><log-viewer v-bind:log=log /></li>
    </ul>
  </div>
</div>
`
});

var offlineLogApp = new Vue({
    el: "#offlineLog",
    data: {
        show: false,
        deviceId: '',
        packageName: '',
    },
    methods: {
        run: function() {
            this.show = true;
        },
        command: function() {
            axios.get(`/api/command/new/${this.deviceId}/${this.packageName}`).then(x => {
                console.log(x);
            }).catch(e => console.log(e));
        },
    },
    template: `
<div class="app" v-if="show">
  <div>
    <span>device id</span>
    <input v-model="deviceId"></input>
  </div>
  <div>
    <span>package name</span>
    <input v-model="packageName"></input>
  </div>
  <button v-on:click="command()">query</button>
</div>
`
});

var statusApp = new Vue({
    el: "#status",
    data: {
        show: false,
        status: {},
    },
    methods: {
        run: function() {
            this.show = true;
            this.queryStatus();
        },
        queryStatus: function() {
            axios.get("/api/status").then(payload => this.status = payload.data).catch(error => console.log(error));
        }
    },
    template: `
<div class="app" v-if="show">
  <table>
    <tr><td>服务器地址</td><td>{{status.hostAddress}}</td></tr>
    <tr><td>端口</td><td>{{status.nodePort}}</td></tr>
    <tr><td>HTTP服务器端口</td><td>{{status.serverPort}}</td></tr>
    <tr><td>其他节点地址</td><td>{{status.peerAddress}}</td></tr>
    <tr><td>PID</td><td>{{status.pid}}</td></tr>
    <tr><td>启动时间</td><td>{{new Date(status.startTime).toLocaleString()}}</td></tr>
  </table>
</div>
`
});

var navigate = new Vue({
    el: "#navigate",
    data: {
        appList: [
            {title: "device", app: deviceApp},
            {title: "log", app: logApp},
            {title: "offlineLog", app: offlineLogApp},
            {title: "status", app: statusApp}
        ]
    },
    methods: {
        navigate: function(app) {
            this.hideAll();
            app.run();
        },
        hideAll: function() {
            for (i in this.appList) {
                this.appList[i].app.show = false;
            }
        }
    },
    template: `
<div class="left navigate">
  <ul class="nomargin nopadding">
    <li v-for="app in appList" class="navigate-item" v-on:click="navigate(app.app)">
      {{ app.title }}
    </li>
  </ul>
</div>
`
});

navigate.navigate(deviceApp);
