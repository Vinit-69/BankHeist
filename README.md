🏦 BankHeist – A Tactical Digital Heist Simulator
Plan. Infiltrate. Simulate. Escape.

BankHeist is a Java + DSA-powered simulation game where you orchestrate high-stakes digital bank heists using algorithmic strategy and object-oriented design.
Design agents, equip tools, analyze security systems modeled as graphs, and simulate mission outcomes using classic DSA techniques like DFS, backtracking, and pathfinding.

## 👥 Team Members

- Aman  – Roll No. [2310991770]
- Amrit – Roll No. [2310991772]
- Viraj – Roll No. [2310992399]
- Vinit - Roll No. [2310992401]


🎯 Project Highlights
🕵️‍♂️ Agent & Tool Management (CRUD) – Build your dream heist crew.

🧠 Smart Infiltration Algorithms – DFS & backtracking to explore entry paths.

🧱 Bank Security as Graphs – Nodes, traps, and challenges await.

💡 Skill Checks & Tools – Combine abilities and resources to navigate obstacles.

🎮 Simulated Heists – Choose your plan and let it play out, turn-by-turn or auto.

💻 Built With
Java 17+

IntelliJ IDEA

JavaFX (for GUI, coming soon)

GitHub for version control

Pure DSA for brains of the operation


## 🛠️ Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/BankHeist.git
   cd BankHeist


2. Open in IntelliJ IDEA

3. File > Open > Select the project folder

4. Make sure Java SDK 17+ is set up

5. Run the application

6. Use IntelliJ to run the Main class (or launcher once GUI is in place)

⚠️ JavaFX not yet required. CLI-based version runs first. GUI version in development.


🚀 How It Works
You are the mastermind.
Assemble agents, equip them with tools, study the security graph of the bank, and simulate the mission.

Each bank is modeled as a graph of SecurityNodes

Your agents have different abilities

Tools give you advantages (e.g., hacking, stealth, brute-force)

The game runs a DFS-based simulation to determine the path and outcome

Heist success depends on:

Right combination of agents/tools

Optimal pathfinding

Security complexity and alert triggers


📁 Project Structure (Planned)
bash
Copy
Edit
BankHeist/
├── src/
│   ├── agents/         # Agent classes and logic
│   ├── tools/          # Tools and abilities
│   ├── security/       # Security graph and node logic
│   ├── engine/         # Simulation engine
│   └── ui/             # CLI (and later, JavaFX GUI)
├── data/               # Sample agents/tools/plans
├── README.md
├── .gitignore
└── build.gradle


🧠 Learning Goals
Apply DSA concepts (Graphs, Backtracking, DFS)

Practice Object-Oriented Design

Learn JavaFX GUI development

Understand how to build simulation-based systems

Use GitHub and Git for real-world project collaboration



📈 Roadmap
 Project setup (IDE + GitHub)

 Implement core classes (Agent, Tool, SecurityNode)

 CLI-based mission planner

 Graph-based simulation engine

 JavaFX GUI integration

 Save/load heist plans

 Optional AI guard (Dijkstra’s pathing)

 Final polishing + Packaging



🤝 Contributing
This is a semester project, but we welcome suggestions, ideas, and pull requests!

Fork the repo

Create your branch (git checkout -b feature/coolFeature)

Commit your changes (git commit -m 'Add coolFeature')

Push to the branch (git push origin feature/coolFeature)

Open a Pull Request



🧨 Inspirations
Watch Dogs

Ocean’s Eleven

Mr. Robot

Your local DSA textbook, reimagined



📜 License
This project is for educational purposes. Use freely, build boldly.
You break into real banks, you're on your own.