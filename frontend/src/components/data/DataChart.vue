<template>
  <div v-if="isChartVisible" class="chart-container">
    <canvas v-if="isChartVisible" id="dataseries-chart"></canvas>
  </div>
</template>

<script>
import Chart from "chart.js/auto";
import "chartjs-adapter-luxon";

export default {
  name: "DataChart",
  data() {
    return {
      chartOptions: {
        maintainAspectRatio: false,
        plugins: {
          legend: {
            position: this.labelsPosition,
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
            title: {
              display: true,
              text: "time",
              font: {
                size: 14,
              },
            },
            ticks: {
              source: "labels",
            },
          },
          y: {
            title: {
              display: true,
              text: "value",
              font: {
                size: 14,
              },
            },
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

    labelsPosition: {
      type: String,
      required: false,
      default() {
        return "bottom";
      },
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
        data: this.chartData,
        options: this.chartOptions,
      });
    }
  },
};
</script>

<style scoped>
.chart-container {
  height: 100rem;
  width: 100%;
}
</style>
