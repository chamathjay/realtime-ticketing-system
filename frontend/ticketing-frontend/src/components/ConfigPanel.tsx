import React, { useState, useEffect } from "react";
// import axios from "axios";
import "../App.css";
import api from "../api";

interface ConfigPanelProps {
  logs: string[];
  setLogs: React.Dispatch<React.SetStateAction<string[]>>;
}

const ConfigPanel: React.FC<ConfigPanelProps> = ({ setLogs }) => {
  const [totalTickets, setTotalTickets] = useState<number>(0);
  const [maxTicketCapacity, setMaxTicketCapacity] = useState<number>(0);
  const [ticketReleaseRate, setReleaseRate] = useState<number>(0);
  const [customerRetrievalRate, setRetrievalRate] = useState<number>(0);
  const [isSimulationRunning, setSimulationRunning] = useState<boolean>(false);

  const [ticketsRemaining, setTicketsRemaining] = useState<number>(0);
  const [ticketsSold, setTicketsSold] = useState<number>(0);
  const [ticketsInPool, setTicketsInPool] = useState<number>(0);

  const backendUrl = "http://localhost:8080/api/ticketing";

  const startSimulation = async () => {
    if (!validateInputs()) {
      alert("Please ensure all inputs are valid.");
      return;
    }

    try {
      setLogs([]);
      const config = {
        totalTickets,
        maxTicketCapacity,
        ticketReleaseRate,
        customerRetrievalRate,
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

  const fetchStatus = async () => {
    try {
      const response = await api.get(`${backendUrl}/status`);
      const { ticketsRemaining, ticketsSold, ticketsInPool } = response.data;
      setTicketsRemaining(ticketsRemaining);
      setTicketsSold(ticketsSold);
      setTicketsInPool(ticketsInPool);
    } catch (error) {
      setLogs((prevLogs) => [...prevLogs, "Error fetching status: " + error]);
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

  const validateInputs = () => {
    return (
      totalTickets > 0 &&
      totalTickets < Number.MAX_SAFE_INTEGER &&
      maxTicketCapacity > 0 &&
      maxTicketCapacity < Number.MAX_SAFE_INTEGER &&
      ticketReleaseRate > 0 &&
      ticketReleaseRate < Number.MAX_SAFE_INTEGER &&
      customerRetrievalRate > 0 &&
      customerRetrievalRate < Number.MAX_SAFE_INTEGER
    );
  };

  useEffect(() => {
    if (isSimulationRunning) {
      const interval = setInterval(() => {
        fetchLogs();
        fetchStatus();
      }, 500);
      return () => clearInterval(interval);
    }
  }, [isSimulationRunning]);

  return (
    <>
      <h1 className="title">Ticketing Simulation</h1>
      <div className="main-container">
        <div className="config-panel">
          <h2>Configure the parameters:</h2>
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
                value={ticketReleaseRate}
                onChange={(e) => setReleaseRate(Number(e.target.value))}
              />
            </label>
            <label>
              {" "}
              Ticket Retrieval Rate by Customers (seconds):
              <input
                type="number"
                placeholder="Customer Retrieval Rate"
                value={customerRetrievalRate}
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
        <div className="status-panel">
          <h2>Ticket Availability Status</h2>
          <div className="tickets-remaining">
            <h4>Tickets Remaining in the System:</h4>
            <p id="tickets-remaining-p"> {ticketsRemaining} </p>
          </div>
          <div className="tickets-sold">
            <h4>Tickets Sold:</h4>
            <p id="tickets-sold-p"> {ticketsSold} </p>
          </div>
          <div className="pool-tickets">
            <h4>Tickets in the pool:</h4>
            <p id="pool-tickets-p"> {ticketsInPool} </p>
          </div>
        </div>
      </div>
    </>
  );
};

export default ConfigPanel;
