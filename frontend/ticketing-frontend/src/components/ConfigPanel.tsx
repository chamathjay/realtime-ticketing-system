import React, { useState, useEffect } from "react";
import axios from "axios";
import "../App.css";
import Logs from "../components/Logs";

const ConfigPanel: React.FC = () => {
  const [totalTickets, setTotalTickets] = useState<number>(0);
  const [maxCapacity, setMaxCapacity] = useState<number>(0);
  const [releaseRate, setReleaseRate] = useState<number>(0);
  const [retrievalRate, setRetrievalRate] = useState<number>(0);
  const [logs, setLogs] = useState<string[]>([]);
  const [isSimulationRunning, setSimulationRunning] = useState<boolean>(false);

  const backendUrl = "http://localhost:8080/api/ticketing";

  const startSimulation = async () => {
    try {
      await axios.post(`${backendUrl}/start`);
      setSimulationRunning(true);
      setLogs((prevLogs) => [...prevLogs, "Simulation started."]);
    } catch (error) {
      setLogs((prevLogs) => [
        ...prevLogs,
        "Error starting simulation: " + error,
      ]);
    }
  };

  const stopSimulation = async () => {
    try {
      await axios.post(`${backendUrl}/stop`);
      setSimulationRunning(false);
      setLogs((prevLogs) => [...prevLogs, "Simulation stopped."]);
    } catch (error) {
      setLogs((prevLogs) => [
        ...prevLogs,
        "Error stopping simulation: " + error,
      ]);
    }
  };

  const fetchLogs = async () => {
    try {
      const response = await axios.get(`${backendUrl}/logs`);
      setLogs(response.data);
    } catch (error) {
      setLogs((prevLogs) => [...prevLogs, "Error fetching logs: " + error]);
    }
  };

  const resetInputsAndLogs = () => {
    setTotalTickets(0);
    setMaxCapacity(0);
    setReleaseRate(0);
    setRetrievalRate(0);
    setLogs([]);
    setSimulationRunning(false);
  };

  useEffect(() => {
    if (isSimulationRunning) {
      const interval = setInterval(fetchLogs, 2000);
      return () => clearInterval(interval);
    }
  }, [isSimulationRunning]);

  return (
    <div className="container">
      <div className="config-panel">
        <h1>Ticketing Simulation</h1>
        <h2>Configue the parameters:</h2>
        <div className="input-section">
          <label>
            Total Number of Tickets in the System:
            <input
              type="number"
              placeholder="Total Tickets"
              value={totalTickets}
              onChange={(e) => setTotalTickets(Number(e.target.value))}
            />
          </label>
          <label>
            {" "}
            Maximum Capacity of the Ticket Pool:
            <input
              type="number"
              placeholder="Max Capacity"
              value={maxCapacity}
              onChange={(e) => setMaxCapacity(Number(e.target.value))}
            />
          </label>
          <label>
            {" "}
            Ticket Release Rate by Vendors (seconds):
            <input
              type="number"
              placeholder="Ticket Release Rate"
              value={releaseRate}
              onChange={(e) => setReleaseRate(Number(e.target.value))}
            />
          </label>
          <label>
            {" "}
            Ticket Retrieval Rate by Customers (seconds):
            <input
              type="number"
              placeholder="Customer Retrieval Rate"
              value={retrievalRate}
              onChange={(e) => setRetrievalRate(Number(e.target.value))}
            />
          </label>
        </div>
        <div className="button-section">
          <button onClick={startSimulation} disabled={isSimulationRunning}>
            Start Simulation
          </button>
          <button onClick={stopSimulation} disabled={!isSimulationRunning}>
            Stop Simulation
          </button>
          <button onClick={resetInputsAndLogs} disabled={isSimulationRunning}>
            Reset
          </button>
        </div>
        <Logs logs={logs} />
      </div>
    </div>
  );
};

export default ConfigPanel;
