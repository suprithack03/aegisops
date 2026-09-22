import { useEffect, useState } from "react";
import "./index.css";

const GATEWAY_URL = "http://localhost:8080";

function getUserFromToken(storedToken) {
    if (!storedToken) {
        return {
            username: "Security User",
            role: "USER"
        };
    }

    try {
        const payload = JSON.parse(
            atob(storedToken.split(".")[1])
        );

        return {
            username: payload.sub || "Security User",
            role: payload.role || "USER"
        };
    } catch (error) {
        return {
            username: "Security User",
            role: "USER"
        };
    }
}

function App() {
    const storedToken = localStorage.getItem("aegisops_token");
    const initialUser = getUserFromToken(storedToken);

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");
    const [message, setMessage] = useState("");
    const [loading, setLoading] = useState(false);

    const [token, setToken] = useState(storedToken);

    const [displayUsername, setDisplayUsername] = useState(
        localStorage.getItem("aegisops_username") ||
        initialUser.username
    );

    const [displayRole, setDisplayRole] = useState(
        localStorage.getItem("aegisops_role") ||
        initialUser.role
    );

    const [incidents, setIncidents] = useState([]);
    const [loadingIncidents, setLoadingIncidents] =
        useState(false);
    const [incidentError, setIncidentError] = useState("");

    async function handleLogin(event) {
        event.preventDefault();

        setMessage("");
        setLoading(true);

        try {
            const response = await fetch(
                `${GATEWAY_URL}/api/auth/login`,
                {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({
                        username,
                        password
                    })
                }
            );

            if (!response.ok) {
                setMessage(
                    `Login failed. HTTP ${response.status}`
                );
                return;
            }

            const data = await response.json();

            if (!data.token) {
                setMessage(
                    "Login failed: token was not returned."
                );
                return;
            }

            localStorage.setItem(
                "aegisops_token",
                data.token
            );

            localStorage.setItem(
                "aegisops_username",
                data.username
            );

            localStorage.setItem(
                "aegisops_role",
                data.role
            );

            setDisplayUsername(data.username);
            setDisplayRole(data.role);

            setToken(data.token);
            setMessage("");
            setPassword("");

        } catch (error) {
            setMessage(
                "Unable to connect to the AegisOps Gateway."
            );
        } finally {
            setLoading(false);
        }
    }

    async function loadIncidents() {
        const storedToken =
            localStorage.getItem("aegisops_token");

        if (!storedToken) {
            return;
        }

        setLoadingIncidents(true);
        setIncidentError("");

        try {
            const response = await fetch(
                `${GATEWAY_URL}/api/incidents`,
                {
                    method: "GET",
                    headers: {
                        Authorization:
                            `Bearer ${storedToken}`
                    }
                }
            );

            if (response.status === 401) {
                handleLogout();
                return;
            }

            if (response.status === 403) {
                setIncidentError(
                    "You are not authorized to view incidents."
                );
                return;
            }

            if (!response.ok) {
                setIncidentError(
                    `Unable to load incidents. HTTP ${response.status}`
                );
                return;
            }

            const data = await response.json();

            setIncidents(data);

        } catch (error) {
            setIncidentError(
                "Unable to connect to the AegisOps Gateway."
            );
        } finally {
            setLoadingIncidents(false);
        }
    }

    function handleLogout() {
        localStorage.removeItem("aegisops_token");
        localStorage.removeItem("aegisops_username");
        localStorage.removeItem("aegisops_role");

        setToken(null);
        setDisplayUsername("Security User");
        setDisplayRole("USER");
        setIncidents([]);
        setIncidentError("");
    }

    useEffect(() => {
        if (token) {
            loadIncidents();
        }
    }, [token]);

    if (!token) {
        return (
            <div className="app-container">
                <div className="login-card">
                    <div className="brand-section">
                        <h1>AegisOps</h1>
                        <p>
                            Security Operations & Threat Response
                        </p>
                    </div>

                    <form onSubmit={handleLogin}>
                        <label htmlFor="username">
                            Username
                        </label>

                        <input
                            id="username"
                            type="text"
                            value={username}
                            onChange={(event) =>
                                setUsername(
                                    event.target.value
                                )
                            }
                            placeholder="Enter username"
                            required
                        />

                        <label htmlFor="password">
                            Password
                        </label>

                        <input
                            id="password"
                            type="password"
                            value={password}
                            onChange={(event) =>
                                setPassword(
                                    event.target.value
                                )
                            }
                            placeholder="Enter password"
                            required
                        />

                        <button
                            type="submit"
                            disabled={loading}
                        >
                            {loading
                                ? "Signing in..."
                                : "Sign in"}
                        </button>
                    </form>

                    {message && (
                        <div className="message">
                            {message}
                        </div>
                    )}
                </div>
            </div>
        );
    }

    const openIncidents =
        incidents.filter(
            (incident) =>
                incident.status === "OPEN"
        ).length;

    const investigatingIncidents =
        incidents.filter(
            (incident) =>
                incident.status === "INVESTIGATING"
        ).length;

    const awaitingApproval =
        incidents.filter(
            (incident) =>
                incident.status === "AWAITING_APPROVAL"
        ).length;

    const highSeverityIncidents =
        incidents.filter(
            (incident) =>
                incident.severity === "HIGH" ||
                incident.severity === "CRITICAL"
        ).length;

    return (
        <div className="dashboard-container">
            <header className="dashboard-header">
                <div>
                    <h1>AegisOps</h1>
                    <p>
                        Security Operations & Threat Response
                    </p>
                </div>

                <div className="user-section">
                    <div>
                        <strong>{displayUsername}</strong>
                        <span>{displayRole}</span>
                    </div>

                    <button
                        className="logout-button"
                        onClick={handleLogout}
                    >
                        Logout
                    </button>
                </div>
            </header>

            <main className="dashboard-main">
                <section className="dashboard-title">
                    <div>
                        <h2>Security Dashboard</h2>
                        <p>
                            Monitor active security incidents
                            and investigation status.
                        </p>
                    </div>

                    <button
                        className="refresh-button"
                        onClick={loadIncidents}
                        disabled={loadingIncidents}
                    >
                        {loadingIncidents
                            ? "Refreshing..."
                            : "Refresh"}
                    </button>
                </section>

                {incidentError && (
                    <div className="message error-message">
                        {incidentError}
                    </div>
                )}

                <section className="summary-grid">
                    <div className="summary-card">
                        <span>Total incidents</span>
                        <strong>
                            {incidents.length}
                        </strong>
                    </div>

                    <div className="summary-card">
                        <span>Open</span>
                        <strong>{openIncidents}</strong>
                    </div>

                    <div className="summary-card">
                        <span>Investigating</span>
                        <strong>
                            {investigatingIncidents}
                        </strong>
                    </div>

                    <div className="summary-card">
                        <span>Awaiting approval</span>
                        <strong>
                            {awaitingApproval}
                        </strong>
                    </div>

                    <div className="summary-card">
                        <span>High / Critical</span>
                        <strong>
                            {highSeverityIncidents}
                        </strong>
                    </div>
                </section>

                <section className="incidents-section">
                    <div className="section-header">
                        <h3>Incidents</h3>
                    </div>

                    {loadingIncidents && (
                        <div className="empty-state">
                            Loading incidents...
                        </div>
                    )}

                    {!loadingIncidents &&
                        incidents.length === 0 &&
                        !incidentError && (
                            <div className="empty-state">
                                No incidents found.
                            </div>
                        )}

                    {!loadingIncidents &&
                        incidents.length > 0 && (
                            <div className="table-container">
                                <table>
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Type</th>
                                            <th>Severity</th>
                                            <th>Status</th>
                                            <th>User</th>
                                            <th>Threat Score</th>
                                        </tr>
                                    </thead>

                                    <tbody>
                                        {incidents.map(
                                            (incident) => (
                                                <tr
                                                    key={
                                                        incident.id
                                                    }
                                                >
                                                    <td>
                                                        {
                                                            incident.id
                                                        }
                                                    </td>

                                                    <td>
                                                        {
                                                            incident.incidentType
                                                        }
                                                    </td>

                                                    <td>
                                                        <span
                                                            className={`severity-badge severity-${String(
                                                                incident.severity
                                                            ).toLowerCase()}`}
                                                        >
                                                            {
                                                                incident.severity
                                                            }
                                                        </span>
                                                    </td>

                                                    <td>
                                                        {
                                                            incident.status
                                                        }
                                                    </td>

                                                    <td>
                                                        {
                                                            incident.username ||
                                                            "—"
                                                        }
                                                    </td>

                                                    <td>
                                                        {
                                                            incident.threatScore
                                                        }
                                                    </td>
                                                </tr>
                                            )
                                        )}
                                    </tbody>
                                </table>
                            </div>
                        )}
                </section>
            </main>
        </div>
    );
}

export default App;