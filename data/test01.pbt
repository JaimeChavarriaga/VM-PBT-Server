{
  "profiles": [
    {"name": "#1", "RAM": 1, "cores": 1},
    {"name": "#2", "RAM": 2, "cores": 2},
    {"name": "#3", "RAM": 4, "cores": 4}
  ],
  "scenarios": [
  	[{"nameProfile": "#2"},{"nameProfile": "#3"}]
  ],
  "numberOfTests": 2,
  "OutputFilename": "testResults1",
  "priorities": ["idle"],
  "clientTests": ["MEMORY", "CPU"],
  "benchmark": ["Integers-Primes-FindNPrimes", 1000],
  "machineNamePrefix": "MachineTest"
}