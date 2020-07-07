package com.immi;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Graph {
    private class Node{
        private String label;
        private List<Edge> edges = new ArrayList<>();
        public Node(String label) {
            this.label = label;
        }

        @Override
        public String toString() {
            return label;
        }

        public void addEdge(Node to){
            edges.add(new Edge(this,to));
        }

        public List<Edge> getEdges(){
            return edges;
        }
    }

    private class Edge{
        private Node from;
        private Node to;

        public Edge(Node from, Node to) {
            this.from = from;
            this.to = to;
        }

        @Override
        public String toString() {
            return to.toString();
        }
    }
    
    private Map<String, Node> nodes = new HashMap<>();
    
    public void addNode(String label){
        nodes.putIfAbsent(label,new Node(label));
    }

    public void addEdge(String from, String to){
        Node fromNode = nodes.get(from);
        Node toNode = nodes.get(to);
        if(fromNode == null || toNode == null)
            throw new IllegalArgumentException();
        fromNode.addEdge(toNode);
        toNode.addEdge(fromNode);
    }

    public void print(){
        for (Node node:nodes.values()) {
            List<Edge> edges = node.getEdges();
            if(!edges.isEmpty())
                System.out.println(node + " is connected to " + edges);
        }
    }

    public boolean hasCycle(){
         Set<Node> visited = new HashSet<>();
         List<Node> cycleTracker = new ArrayList<>();
         for (Node node: nodes.values()){
            if(!visited.contains(node)) {
            	if(hasCycle(node, null, visited,cycleTracker)) {
            		for (Node nodeInCycle : cycleTracker) {
						System.out.print(nodeInCycle +" -> ");
					}
            		System.out.print(node);
            		System.out.println();
            		return true;
            	}
            }
            cycleTracker.clear();
         }
         return false;
    }

    private  boolean hasCycle(Node node, Node parent, Set<Node> visited,List<Node> cycleTracker){
         cycleTracker.add(node);
         visited.add(node);
         for (Edge edge:node.getEdges()) {
            if(edge.to == parent)
                continue;
            if(visited.contains(edge.to) || hasCycle(edge.to,node,visited,cycleTracker))
                return true;
         }
         return false;
    }
}
