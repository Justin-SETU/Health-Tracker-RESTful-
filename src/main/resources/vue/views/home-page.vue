<!-- the "home-page" element is passed as a parameter to VueComponent in the JavalinConfig file -->
<template id="home-page">
  <app-layout>
    <div class="row">
      <div class="col">
        <div class="card">
          <h5 class="card-header">Registered Users</h5>
          <div class="card-body">
            <h5 class="card-title">{{users.length}} users</h5>
            <a href="/users" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="col">
        <div class="card">
          <h5 class="card-header">Activities Summary</h5>
          <div class="card-body">
            <h5 class="card-title">{{activities.length}} activities</h5>
            <a href="/activities" class="btn btn-primary">More Details...</a>
          </div>
        </div>
      </div>
      <div class="row mt-3">
        <div class="col">
          <div class="card">
            <h5 class="card-header">BMI Summary</h5>
            <div class="card-body">
              <h5 class="card-title">{{bmis.length}} BMI records</h5>
              <a href="/bmi" class="btn btn-primary">More Details...</a>
            </div>
          </div>
        </div>
        <div class="col">
          <div class="card">
            <h5 class="card-header">Steps Summary</h5>
            <div class="card-body">
              <h5 class="card-title">{{steps.length}} steps records</h5>
              <a href="/step" class="btn btn-primary">More Details...</a>
            </div>
          </div>
        </div>

    </div>
  </app-layout>
</template>

<script>
app.component('home-page',
    {
      template: "#home-page",
      data: () => ({
        users: [],
        activities: [],
        bmis: [],
        steps: []
      }),
      created() {
        axios.get("/api/users")
            .then(res => this.users = res.data)
            .catch(() => alert("Error while fetching users"));
        axios.get("/api/activities")
            .then(res => this.activities = res.data)
            .catch(() => alert("Error while fetching activities"));
        axios.get("/api/bmi")
            .then(res => this.bmis = res.data)
            .catch(() => alert("Error while fetching bmis"));
        axios.get("/api/steps")
            .then(res => this.steps = res.data)
            .catch(() => alert("Error while fetching steps"));
      }
    });
</script>