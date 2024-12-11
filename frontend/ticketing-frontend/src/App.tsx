import React, { useState } from "react";
import ConfigPanel from "./components/ConfigPanel";
import Logs from "./components/Logs";

const App: React.FC = () => {
  const [logs, setLogs] = useState<string[]>([]);

  return (
    <div className="app-container">
      <div className="config-container">
        <ConfigPanel logs={logs} setLogs={setLogs} />
      </div>
      <div className="logs-container">
        <Logs logs={logs} />
      </div>
    </div>
  );
};

export default App;
