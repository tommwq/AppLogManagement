function formatUnknownLog(log) {
    var seq = log.header.sequence;
    var time = log.header.time;

    return [seq.toString(),
            new Date(time).toLocaleString(),
            "UNKNOWN LOG"
           ].join(" ");
}

function formatDeviceAndAppInfoLog(log) {
    console.log(log);
    var seq = log.header.sequence;
    var time = log.header.time;
    var deviceVersion = log.body.deviceVersion;
    var baseOsName = log.body.baseOsName;
    var baseOsVersion = log.body.baseOsVersion;
    var osName = log.body.osName;
    var osVersion = log.body.osVersion;
    var appVersion = log.body.appVersion;

    var part = [seq.toString(),
                new Date(time).toLocaleString(),
                deviceVersion,
                baseOsName,
                baseOsVersion,
                osName,
                osVersion,
                appVersion,
               ];
    
    var moduleInfoArray = log.body.moduleInfo;
    for (var i in moduleInfoArray) {
        var name = moduleInfoArray[i].moduleName;
        var version = moduleInfoArray[i].moduleVersion;
        part.push(name);
        part.push(version);
    }

    return part.join(" ");
}

function formatMethodAndObjectInfoLog(log) {
    console.log(log);
}

function formatExceptionInfoLog(log) {
    console.log(log);
}

function formatUserDefinedLog(log) {
    console.log(log);
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
                content: formatLog(this.log)
            };
        }
    },
    props: ["log"],
    template: String.raw`
<span>{{content}}</span>
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
    template: String.raw`
<div v-if="show" class="app">
  <div class="left first-column">
    <p>online device</p>
    <p v-if="deviceIdList.length == 0">NO ONLINE DEVICE</p>
    <ul class="nomarigin nopadding">
      <li v-for="deviceId in deviceIdList" v-on:click="queryDeviceLog(deviceId)" class="device-item">
        {{deviceId}} 
      </li>
    </ul>
  </div>
  <div class="left second-column">
    <p>device log</p>
    <p v-if="logList.length == 0">NO DEVICE LOG</p>
    <ul class="nomarigin nopadding log-list">
      <li v-for="log in logList" class="log-item"><log-viewer v-bind:log=log></log-viewer></li>
    </ul>
  </div>
</div>
`
});

var logApp = new Vue({
    el: "#log",
    data: {
        show: false,
    },
    methods: {
        run: function() {
            this.show = true;
        }
    },
    template: String.raw`
<div class="app" v-if="show">
TODO
</div>
`
});

var offlineLogApp = new Vue({
    el: "#offlineLog",
    data: {
        show: false,
    },
    methods: {
        run: function() {
            this.show = true;
        }
    },
    template: String.raw`
<div class="app" v-if="show">
TODO
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
    template: String.raw`
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
    template: String.raw`
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
