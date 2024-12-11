import React, { useState, useEffect } from "react";
// import axios from "axios";
import "../App.css";
import api from "../api";

interface ConfigPanelProps {
  logs: string[];
  setLogs: React.Dispatch<React.SetStateAction<string[]>>;
}

const ConfigPanel: React.FC<ConfigPanelProps> = ({ setLogs }) => {
  const [totalTickets, setTotalTickets] = useState<number>(10);
  const [maxTicketCapacity, setMaxTicketCapacity] = useState<number>(7);
  const [releaseRate, setReleaseRate] = useState<number>(1);
  const [retrievalRate, setRetrievalRate] = useState<number>(2);
  const [isSimulationRunning, setSimulationRunning] = useState<boolean>(false);

  const backendUrl = "http://localhost:8080/api/ticketing";

  const startSimulation = async () => {
    try {
      setLogs([]);
      const config = {
        totalTickets,
        maxTicketCapacity,
        releaseRate,
        retrievalRate,
      };
      console.log("Simulation Config: ", config);
      await api.post(`${backendUrl}/start`, config);

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
      await api.post(`${backendUrl}/stop`);
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
      const response = await api.get(`${backendUrl}/logs`);
      setLogs(response.data);
    } catch (error) {
      setLogs((prevLogs) => [...prevLogs, "Error fetching logs: " + error]);
    }
  };

  const resetInputsAndLogs = () => {
    try {
      setTotalTickets(0);
      setMaxTicketCapacity(0);
      setReleaseRate(0);
      setRetrievalRate(0);
      setLogs([]);
      setSimulationRunning(false);
    } catch (error) {
      setLogs((prevLogs) => [
        ...prevLogs,
        "Error resetting simulation: " + error,
      ]);
    }
  };

  useEffect(() => {
    if (isSimulationRunning) {
      const interval = setInterval(fetchLogs, 1000);
      return () => clearInterval(interval);
    }
  }, [isSimulationRunning]);

  return (
    <>
      <h1 className="title">Ticketing Simulation</h1>
      <div className="container">
        <div className="config-panel">
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
                value={maxTicketCapacity}
                onChange={(e) => setMaxTicketCapacity(Number(e.target.value))}
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
              Start
            </button>
            <button onClick={stopSimulation} disabled={!isSimulationRunning}>
              Stop
            </button>
            <button onClick={resetInputsAndLogs} disabled={isSimulationRunning}>
              Reset
            </button>
          </div>
        </div>
        <div className="status-section">
          <h2>Ticket Availability Status</h2>
          <div className="available-tickets">
            <h4>Available tickets in the System:</h4>
            <p id="available-tickets-p">10</p>
          </div>
          <div className="tickets-sold">
            <h4>Tickets Sold:</h4>
            <p id="tickets-sold-p">3</p>
          </div>
          <div className="pool-tickets">
            <h4>Tickets in the pool:</h4>
            <p id="pool-tickets-p">6</p>
          </div>
        </div>
      </div>
    </>
  );
};

export default ConfigPanel;
