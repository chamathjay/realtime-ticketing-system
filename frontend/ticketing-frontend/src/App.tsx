import React, { useState } from "react";
import ConfigPanel from "./components/ConfigPanel";
import Logs from "./components/Logs";

const App: React.FC = () => {
  const [logs, setLogs] = useState<string[]>([]);

  return (
    <div className="app-container">
      <ConfigPanel logs={logs} setLogs={setLogs} />
      <Logs logs={logs} />
    </div>
  );
};

export default App;
