// Task Options
option task = {
  name: "secondary-downsampler",
  every: 1h,
}

// Defines a data source
data = from(bucket: "data-downsampled")
  |> range(start: -task.every)
  |> filter(fn: (r) => r._measurement == "deviceData")

data
  // Windows and aggregates the data in to 1h averages
  |> aggregateWindow(fn: mean, every: 1h)
  |> to(bucket: "data-downsampled2", org: "my-org")