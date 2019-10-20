new Vue({
    el: "#app",
    data() {
        return {
            members: [],
            tasks: []
        };
    },
    mounted() {
        axios
            .get("http://localhost:8080/membersapi")
            .then(response => (this.members = response.data));
        axios
            .get("http://localhost:8080/tasksapi")
            .then(response => (this.tasks = response.data));
    }
});