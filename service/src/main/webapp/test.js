var app = new Vue({
    el: "#app",
    data: {
        nodeStatus: {},
        deviceId: ""
    },
    methods: {
        queryNodeStatus: function() {
            axios.get("/api/status")
                .then(payload => this.nodeStatus = payload.data)
                .catch(error => console.log(error));
        },
        test: function() {
            axios.get("/api/lookup/" + this.deviceId)
                .then(payload => console.log(payload))
                .catch(error => console.log(error));
        }
    }
});

app.queryNodeStatus();


