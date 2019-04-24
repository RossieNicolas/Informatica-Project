package com.architec.demo.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.sql.DataSource;

import com.architec.demo.models.Fiche;;

public class JDBCcontroller {
    private DataSource dataSource;

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void insert(Fiche fiche) {

        String sql = "INSERT INTO ASSIGNMENT "
                + "(ficheID, title, binnenlands, description, amount_hours, amount_students, start_date, end_date, given_by) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection conn = null;

        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, fiche.getFicheID());
            ps.setString(2, fiche.getTitle());
            ps.setInt(3, fiche.getBinnenland());
            ps.setString(4, fiche.getDescription());
            ps.setInt(5, fiche.getAmount_hours());
            ps.setInt(6, fiche.getAmount_students());
            ps.setString(7, fiche.getStart_date().toString());
            ps.setString(8, fiche.getEnd_date().toString());
            ps.setString(9, fiche.getGiven_by());
            ps.executeUpdate();
            ps.close();

        } catch (SQLException e) {
            throw new RuntimeException(e);

        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
    }

    public String findAll() {
        String sql = "SELECT * FROM ASSIGNMENT";
        Connection conn = null;
        String output = "";
        try {
            conn = dataSource.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            output = toHTML(ps.executeQuery());
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        return output;
    }

    private String toHTML(ResultSet input) {
        String output = "<table><tr><th>Nummer</th><th>Opdrachtgever</th><th>type</th><th>Begin</th><th>Einde</th><th>Gevalideerd</th><th>Volzet</th></tr>";

        try {

            ResultSetMetaData metadata = input.getMetaData();
            int numberOfColumns = metadata.getColumnCount();

            ArrayList<String> arrayList = new ArrayList<String>();
            while (input.next()) {
                int i = 1;
                while (i <= numberOfColumns) {
                    arrayList.add(input.getString(i++));
                }
            }

            for (int i = 0; i <= numberOfColumns; i++) {
                output += "<tr><td>" + input.getInt(1) + "</td><td>" + input.getString(14) + "</td><td>"
                        + input.getString(3) + "</td><td>" + input.getDate(9) + "</td><td>" + input.getDate(10)
                        + "</td>";

                if (input.getByte(12) == 1) {
                    output += "<td>Ja</td>";
                } else {
                    output += "<td>Nee</td>";
                }

                if (input.getInt(7) > input.getInt(8)) {
                    output += "<td>Ja</td>";
                } else {
                    output += "<td>Nee</td>";
                }

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return output + "</tr></table>";
    }
}