import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.algs4.*;
import edu.princeton.cs.introcs.*;

public class BaseballElimination {

	private int numberOfTeams = 0;
	private List<String> teams;
	private int[] certiNumberOfElim;
	private int[][] certiIndexOfElim;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] against;
	private boolean[] isEliminated;
	private int indexOfGameVertex[][];
	private int indexOfTeamVertex[];

	// create a baseball division from given filename in format specified below
	public BaseballElimination(String filename) {
		In in = new In(filename);
		numberOfTeams = in.readInt();

		teams = new ArrayList<String>(numberOfTeams);

		certiNumberOfElim = new int[numberOfTeams];
		certiIndexOfElim = new int[numberOfTeams][numberOfTeams];
		
		wins = new int[numberOfTeams];
		losses = new int[numberOfTeams];
		remaining = new int[numberOfTeams];
		against = new int[numberOfTeams][numberOfTeams];
		isEliminated = new boolean[numberOfTeams];
		
		for (int i = 0; i < numberOfTeams; i++) {
			String teamname = in.readString();
			teams.add(teamname);
			wins[i] = in.readInt();
			losses[i] = in.readInt();
			remaining[i] = in.readInt();
			for (int j = 0; j < numberOfTeams; j++) {
				against[i][j] = in.readInt();
			}
		}
		
		indexOfGameVertex = new int[numberOfTeams][numberOfTeams];
		indexOfTeamVertex = new int[numberOfTeams];
		
		processAllElimination();
	}

	// number of teams
	public int numberOfTeams() {
		return numberOfTeams;
	}
	
	// all teams
	public Iterable<String> teams() {
		return teams;
	}
	
	private int getIndexOfTeam(String team) {
		if (teams.contains(team) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team!");
		}
		return teams.indexOf(team);
	}
	
	// number of wins for given team
	public int wins(String team) {
		if (teams.contains(team) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team!");
		}
		int indexOfTeam = getIndexOfTeam(team);
		return wins[indexOfTeam];
	}
	
	// number of losses for given team
	public int losses(String team) {
		if (teams.contains(team) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team!");
		}
		int indexOfTeam = getIndexOfTeam(team);
		return losses[indexOfTeam];
	}
	
	// number of remaining games for given team
	public int remaining(String team) {
		if (teams.contains(team) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team!");
		}
		int indexOfTeam = getIndexOfTeam(team);
		return remaining[indexOfTeam];
	}
	
	// number of remaining games between team1 and team2
	public int against(String team1, String team2) {
		if (teams.contains(team1) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team: " + team1);
		}
		if (teams.contains(team2) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team: " + team2);
		}
		int indexOfTeam1 = getIndexOfTeam(team1);
		int indexOfTeam2 = getIndexOfTeam(team2);
		return against[indexOfTeam1][indexOfTeam2];
	}
	
	// is given team eliminated?
	public boolean isEliminated(String team) {
		if (teams.contains(team) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team!");
		}
		int indexOfTeam = getIndexOfTeam(team);
		return isEliminated[indexOfTeam];
	}
	
	private int getIndexOfGameVertex(int teamIndex1, int teamIndex2) {
		if (teamIndex1 < 0 || teamIndex1 >= numberOfTeams) {
			throw new java.lang.IllegalArgumentException("Invalid input team index: " + teamIndex1);
		}
		if (teamIndex2 < 0 || teamIndex2 >= numberOfTeams) {
			throw new java.lang.IllegalArgumentException("Invalid input team index: " + teamIndex2);
		}
		if (teamIndex1 == teamIndex2) {
			throw new java.lang.IllegalArgumentException("Input team indexes must not be equal");
		}
		if (teamIndex1 > teamIndex2) {
			return indexOfGameVertex[teamIndex2][teamIndex1];
		}
		else {
			return indexOfGameVertex[teamIndex1][teamIndex2];
		}
	}
	
	private void processElimination(String team) {
		int teamIndex = getIndexOfTeam(team);
		
		// first round: trivial elimination
		for (int i = 0; i < numberOfTeams; i++) {
			if (i == teamIndex) {
				continue;
			}
			if (wins[teamIndex] + remaining[teamIndex] - wins[i] < 0) {
				isEliminated[teamIndex] = true;
				certiNumberOfElim[teamIndex] = 1;
				certiIndexOfElim[teamIndex][0] = i;
				
				return;
			}
		}
		
		// second round: nontrivial elimination
		int gameVertexIndex;
		int fullFlowValue = 0;
		int startIndex = 0;
		int sourceIndex = startIndex++;
		// assign game vertices index
		for (int i = 0; i < numberOfTeams; i++) {
			for (int j = i+1; j < numberOfTeams; j++) {
				if (i == teamIndex || j == teamIndex) {
					continue;
				}
				indexOfGameVertex[i][j] = startIndex++;
			}
		}
		// assign team vertices index
		for (int i = 0; i < numberOfTeams; i++) {
			if (i == teamIndex) {
				continue;
			}
			indexOfTeamVertex[i] = startIndex++;
		}
		int targetIndex = startIndex++;
		
		FlowNetwork FN = new FlowNetwork(startIndex);
		
		// assign edges from source to game vertices
		for (int i = 0; i < numberOfTeams; i++) {
			for (int j = i+1; j < numberOfTeams; j++) {
				if (i == teamIndex || j == teamIndex) {
					continue;
				}
				gameVertexIndex = getIndexOfGameVertex(i, j);
				FlowEdge FE = new FlowEdge(sourceIndex, gameVertexIndex, against[i][j]);
				FN.addEdge(FE);
				fullFlowValue += against[i][j];
			}
		}
		// assign edges from game vertices to team vertices
		for (int i = 0; i < numberOfTeams; i++) {
			for (int j = i+1; j < numberOfTeams; j++) {
				if (i == teamIndex || j == teamIndex) {
					continue;
				}
				gameVertexIndex = getIndexOfGameVertex(i, j);
				FlowEdge FE1 = new FlowEdge(gameVertexIndex, indexOfTeamVertex[i], Double.POSITIVE_INFINITY);
				FlowEdge FE2 = new FlowEdge(gameVertexIndex, indexOfTeamVertex[j], Double.POSITIVE_INFINITY);
				
				FN.addEdge(FE1);
				FN.addEdge(FE2);
			}
		}
		
		// assign edges from team vertices to target
		for (int i = 0; i < numberOfTeams; i++) {
			if (i == teamIndex) {
				continue;
			}
			FlowEdge FE = new FlowEdge(indexOfTeamVertex[i], targetIndex, wins[teamIndex] + remaining[teamIndex] - wins[i]);
			FN.addEdge(FE);
		}
		
		FordFulkerson FF = new FordFulkerson(FN, sourceIndex, targetIndex);
		int maxFlowValue = (int)FF.value();
		if (maxFlowValue == fullFlowValue) {
			isEliminated[teamIndex] = false;
		} else {
			isEliminated[teamIndex] = true;
			certiNumberOfElim[teamIndex] = 0;
			for (int i = 0; i<FN.V(); i++) {
				if (FF.inCut(i)) {
					// interpret team index from team vertex index
					for (int j = 0; j < numberOfTeams; j++) {
						if (j == teamIndex) {
							continue;
						}
						if (i == indexOfTeamVertex[j]) {
							certiIndexOfElim[teamIndex][certiNumberOfElim[teamIndex]] = j;
							certiNumberOfElim[teamIndex]++;
						}
					}
				}
			}
		}
	}
	
	private void processAllElimination() {
		for (String team : teams()) {
			processElimination(team);
		}
	}
	
	// subset R of teams that eliminates given team; null if not eliminated
	public Iterable<String> certificateOfElimination(String team) {
		if (teams.contains(team) == false) {
			throw new java.lang.IllegalArgumentException("Invalid input team!");
		}
		int indexOfTeam = getIndexOfTeam(team);
		if (isEliminated[indexOfTeam] == false) {
			return null;
		} else {
			ArrayList<String> certiOfElim = new ArrayList<String>();
			for (int i = 0; i < certiNumberOfElim[indexOfTeam]; i++) {
				certiOfElim.add(teams.get(certiIndexOfElim[indexOfTeam][i]));
			}
			return certiOfElim;
		}
	}
	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	                StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}

}
