<template>
  <div v-if="isChartVisible">
    <canvas id="dataseries-chart" height="300"></canvas>
  </div>
</template>

<script>
import Chart from "chart.js/auto";
import "chartjs-adapter-luxon";
// import "chartjs-adapter-date-fns";
// import { pl } from "date-fns/locale";
// import { Line } from "vue3-chart-v2";

export default {
  name: "DataChart",
  // extends: Line,
  data() {
    return {
      chartOptions: {
        plugins: {
          legend: {
            position: "bottom",
          },
        },
        layout: {
          padding: 5,
        },
        scales: {
          x: {
            type: "time",
            time: {
              unit: "second",
              displayFormats: {
                second: "D H:mm:ss",
              },
            },
            ticks: {
              source: "labels",
            },
            // adapters: {
            //   date: {
            //     locale: pl,
            //   },
            // },
          },
        },
      },

      myChart: {},
    };
  },
  props: {
    plotData: {
      type: Object,
      required: true,
      default() {
        return {
          start: {},
          end: {},
          datasets: [],
        };
      },
    },

    xLabelsNumber: {
      type: Number,
      required: false,
      default: 4,
    },
  },

  computed: {
    dividedLabels() {
      const result = [this.plotData.start];
      const delta = Math.round(
        (this.plotData.end - this.plotData.start) / this.xLabelsNumber
      );

      for (let i = 1; i < this.xLabelsNumber; i++)
        result.push(this.plotData.start + delta * i);

      result.push(this.plotData.end);

      return result;
    },

    chartData() {
      return {
        datasets: this.plotData.datasets,
        labels: this.dividedLabels,
      };
    },

    isChartVisible() {
      return (
        Array.isArray(this.plotData.datasets) &&
        this.plotData.datasets.length > 0
      );
    },
  },
  mounted() {
    if (this.isChartVisible) {
      this.myChart = new Chart(document.getElementById("dataseries-chart"), {
        type: "line",
        data: {
          datasets: this.plotData.datasets,
          labels: this.dividedLabels,
        },
        options: this.chartOptions,
      });
      // this.renderChart(this.chartData, this.chartOptions);
    }
  },
};
</script>

<style scoped>
#dataseries-chart {
  min-height: 1vh;
}
</style>
