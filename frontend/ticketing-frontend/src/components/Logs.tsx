import React from "react";
import "../App.css";

interface LogsProps {
  logs: string[];
}

const Logs: React.FC<LogsProps> = ({ logs }) => {
  return (
    <div className="logs-section">
      <h2>Logs</h2>
      <div className="logs">
        {logs.length > 0 ? (
          logs.map((log, index) => <p key={index}>{log}</p>)
        ) : (
          <p>No logs available.</p>
        )}
      </div>
    </div>
  );
};

export default Logs;
