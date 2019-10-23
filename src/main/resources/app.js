new Vue({
    el: "#app",
    data: {
        members: [],
        tasks: []
    },
    mounted() {
        let membersResult;
        axios
            .get("http://localhost:8080/membersapi")
            .then(response => (this.members = response.data));
        axios
            .get("http://localhost:8080/tasksapi")
            .then(response => (this.tasks = response.data));
    }
});