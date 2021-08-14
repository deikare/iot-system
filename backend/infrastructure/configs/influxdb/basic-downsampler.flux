// Task Options
option task = {
  name: "basic-downsampler",
  every: 30m,
}

// Defines a data source
data = from(bucket: "data")
  |> range(start: -task.every)
  |> filter(fn: (r) => r._measurement == "data")

data
  // Windows and aggregates the data in to 10m averages
  |> aggregateWindow(fn: mean, every: 30m)
  |> to(bucket: "data-downsampled", org: "my-org")