import React from "react";

function SideBar() {
  return (
    <div className="sidebar">
      <h2>BankEngine</h2>

      <ul>
        <li>Dashboard</li>
        <li>Accounts</li>
        <li>Transactions</li>
        <li>Failed Transactions</li>
        <li>Performance</li>
      </ul>
    </div>
  );
}

export default SideBar;