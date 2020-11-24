<template>
  <div class="container">
    <line-chart
      style ="background-color: black;"
      v-if="loaded"
      :chartdata="chartdata"
      :options="options"/>
  </div>
</template>

<script>
import LineChart from '@/components/Chart.vue'
import axios from "@/plugins/axios";
export default {
  name: 'Simple',
  components: { LineChart },
  data: () => ({
    loaded: false,
    chartdata:  {
        datasets: []
    },
    options: {
        responsive: true,
        title: {
            display: true,
            text: '퀴즈 성장률',
            fontColor:"white"
        },
        tooltips: {
            mode: 'index',
            intersect: false,
        },
        hover: {
            mode: 'nearest',
            intersect: true
        },
        scales: {
            xAxes: [{
                display: true,
                ticks: {
                  stepSize : 1,
                  fontColor : "white",
					      },
                scaleLabel: {
                    display: true,
                    labelString: '회차'
                }
            }],
            yAxes: [{
                display: true,
                ticks: {
						      min: 0,
                  max: 100,
                  stepSize : 10,
                  fontColor : "white",
					      },
                scaleLabel: {
                    display: true,
                    labelString: '점수(%)',
                    
                }
            }]
        }
    }
  }),
  async mounted () {
    this.loaded = false
    var myColor = ['rgba(255, 99, 132, 0.8)',
                'rgba(54, 162, 235, 0.8)',
                'rgba(255, 206, 86, 0.8)',
                'rgba(75, 192, 192, 0.8)',
                'rgba(153, 102, 255, 0.8)',
                'rgba(255, 159, 64,0.8)']
    try {
      var t = 1;
      await axios
        .get("/newuser/quiz-log").
        then((res)=> {
          console.log(res);
          for (const [key,value] of Object.entries(res.data)) {
            var temp = {}
            var sdata=[];
            var k = 1;
            
            for(const [,value2] of Object.entries(value)){
              sdata.push({x: k++,y:value2.accRate})
            }
            temp["label"] = key;
            temp["data"] = sdata;
            temp["fill"] = false;
            temp["lineTension"] = 0;
            temp["borderColor"] = myColor[(t++)%6];
            temp["showLine"] = true;
            this.chartdata.datasets.push(temp);
          }
        })
      console.log(this.chartdata.datasets);
      this.loaded = true
    } catch (e) {
      console.error(e)
    }
  }
}
</script>