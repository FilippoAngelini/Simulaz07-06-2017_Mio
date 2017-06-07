package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {
	
	public List<Season> listSeasons() {
		String sql = "SELECT season, description FROM seasons" ;
		
		List<Season> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Season(res.getInt("season"), res.getString("description"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Team> listTeams() {
		String sql = "SELECT team FROM teams" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				result.add( new Team(res.getString("team"))) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Team> getTeamsForSeason(Season stagione, Map<String, Team> mapTeams) {
		
		String sql = "SELECT homeTeam, awayTeam FROM matches WHERE season = ?" ;
		
		List<Team> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, stagione.getSeason());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Team h = mapTeams.get(res.getString("homeTeam"));
				if(!result.contains(h))
					result.add(h) ;
				Team a = mapTeams.get(res.getString("awayTeam"));
				if(!result.contains(a))
					result.add( a) ;
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<Match> listMatchesForSeason(Season stagione, Map<String, Team> mapTeams) {
		String sql = "SELECT match_id, homeTeam, awayTeam, ftr FROM matches WHERE season = ?" ;
		
		List<Match> result = new ArrayList<>() ;
		
		Connection conn = DBConnect.getConnection() ;
		
		try {
			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, stagione.getSeason());
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				Match match = new Match (res.getInt("match_id"), stagione,mapTeams.get(res.getString("homeTeam")), mapTeams.get(res.getString("awayTeam")),  res.getString("ftr"));
				result.add(match);
			}
			
			conn.close();
			return result ;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}


}
