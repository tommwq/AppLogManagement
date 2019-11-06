import Vue from 'vue';
import ElementUI from 'element-ui';

Vue.use(ElementUI);

var app = new Vue({
    el: '#app',
    data: {
        message: 'Hello, Vue!'
    },
    template: `
<el-container>
  <el-header>
header
  </el-header>
  <el-aside>
aside
  </el-aside>
  <el-main>
main
  </el-main>
</el-container>
    `
//    template: '<span>{{message}}</span>'
});
