/*
 * Copyright (C) 2012 http://software-talk.org/ (developer@software-talk.org)
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version. This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details. You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

/*
 * // TODO possible optimizations: - calculate f as soon as g or h are set, so
 * it will not have to be calculated each time it is retrieved - store nodes in
 * openList sorted by their f value.
 */

package pathfinding;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.joml.Vector2i;

import engine.Engine;
import physics.Handler;
import utils.Utils;

/**
 * This class represents a simple map.
 * <p>
 * It's width as well as hight can be set up on construction.
 * The map can represent nodes that are walkable or not, it can be printed
 * to sto, and it can calculate the shortest path between two nodes avoiding
 * walkable nodes.
 * <p>
 * <p>
 * Usage of this package:
 * Create a node class which extends AbstractNode and implements the sethCosts
 * method.
 * Create a NodeFactory that implements the NodeFactory interface.
 * Create Map instance with those created classes.
 * @see ExampleUsage ExampleUsage
 * <p>
 *
 * @see AbstractNode
 * @see NodeFactory
 * @version 1.0
 * @param <T>
 */
public class Map<T extends AbstractNode>
{

	/** weather or not it is possible to walk diagonally on the map in general. */
	protected static boolean CANMOVEDIAGONALY = true;

	/** holds nodes. first dim represents x-, second y-axis. */
	private T[][] nodes;

	/** width + 1 is size of first dimension of nodes. */
	protected int width;
	/** higth + 1 is size of second dimension of nodes. */
	protected int heigth;

	/** a Factory to create instances of specified nodes. */
	private NodeFactory nodeFactory;

	/**
	 * constructs a squared map with given width and hight.
	 * <p>
	 * The nodes will be instanciated througth the given nodeFactory.
	 *
	 * @param width
	 * @param heigth
	 * @param nodeFactory
	 */
	public Map(int width, int heigth, NodeFactory nodeFactory)
	{
		// TODO check parameters. width and heigth should be > 0.
		this.nodeFactory = nodeFactory;
		nodes = (T[][])new AbstractNode[width][heigth];
		this.width = width - 1;
		this.heigth = heigth - 1;
		initEmptyNodes();
	}

	/**
	 * initializes all nodes. Their coordinates will be set correctly.
	 */
	private void initEmptyNodes()
	{
		for(int i = 0; i <= width; i++)
		{
			for(int j = 0; j <= heigth; j++)
			{
				nodes[i][j] = (T)nodeFactory.createNode(i, j);
			}
		}
	}

	/**
	 * sets nodes walkable field at given coordinates to given value.
	 * <p>
	 * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
	 *
	 * @param x
	 * @param y
	 * @param bool
	 */
	public void setWalkable(int x, int y, boolean bool)
	{
		// TODO check parameter.
		nodes[x][y].setWalkable(bool);
	}

	/**
	 * returns node at given coordinates.
	 * <p>
	 * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
	 *
	 * @param x
	 * @param y
	 * @return node
	 */
	public final T getNode(int x, int y)
	{
		// TODO check parameter.
		return nodes[x][y];
	}

	/**
	 * prints map to sto. Feel free to override this method.
	 * <p>
	 * a player will be represented as "o", an unwakable terrain as "#".
	 * Movement penalty will not be displayed.
	 */
	public void drawMap()
	{
		for(int i = 0; i <= width; i++)
		{
			print(" _"); // boarder of map
		}
		print("\n");

		for(int j = heigth; j >= 0; j--)
		{
			print("|"); // boarder of map
			for(int i = 0; i <= width; i++)
			{
				if(nodes[i][j].isWalkable())
				{
					print("  ");
				}
				else
				{
					print(" #"); // draw unwakable
				}
			}
			print("|\n"); // boarder of map
		}

		for(int i = 0; i <= width; i++)
		{
			print(" _"); // boarder of map
		}
	}

	/**
	 * prints something to sto.
	 */
	private void print(String s)
	{
		System.out.print(s);
	}

	/* Variables and methodes for path finding */

	// variables needed for path finding

	/** list containing nodes not visited but adjacent to visited nodes. */
	private List<T> openList;
	/** list containing nodes already visited/taken care of. */
	private List<T> closedList;
	/** done finding path? */
	private boolean done = false;

	/**
	 * finds an allowed path from start to goal coordinates on this map.
	 * <p>
	 * This method uses the A* algorithm. The hCosts value is calculated in
	 * the given Node implementation.
	 * <p>
	 * This method will return a LinkedList containing the start node at the
	 * beginning followed by the calculated shortest allowed path ending
	 * with the end node.
	 * <p>
	 * If no allowed path exists, an empty list will be returned.
	 * <p>
	 * <p>
	 * x/y must be bigger or equal to 0 and smaller or equal to width/hight.
	 *
	 * @param oldX
	 * @param oldY
	 * @param newX
	 * @param newY
	 * @return
	 */
	public final List<T> findPath(int oldX, int oldY, int newX, int newY)
	{
		int l = 0;
		if((newX < 0) || (newX > width) || (newY < 0) || (newY > heigth))
		{
			return new LinkedList<>();
		}
		if((oldX == newX) && (oldY == newY))
		{
			return new LinkedList<>();
		}
		long start = System.currentTimeMillis();
		// TODO check input
		openList = new LinkedList<>();
		closedList = new LinkedList<>();
		openList.add(nodes[oldX][oldY]); // add starting node to open list

		if(!nodes[newX][newY].isWalkable())
		{
			// if goal is not walkable then
			// for each node in map, if adjacent node is walkable, go to
			// adjacent node;

			// get the nearest adjacent walkable node
			Vector2i pos = getAdjacentNodeAround(newX, newY);

			// if( node equals to start node, return empty path
			if((oldX == pos.x) && (oldY == pos.y))
			{
				return new LinkedList<>();
			}

			// else path ends at this adjacent point
			newX = pos.x;
			newY = pos.y;
		}

		done = false;
		T current;
		while(!done)
		{
			// variable to handle to far path searching
			l++;
			// if algorithm take too long to find path, return no paths
			if(((System.currentTimeMillis() - start) > 100) || (l > 200))
			{
				return new LinkedList<>();
			}
			current = lowestFInOpen(); // get node with lowest fCosts from
										// openList
			closedList.add(current); // add current node to closed list
			openList.remove(current); // delete current node from open list

			if((current.getxPosition() == newX) && (current.getyPosition() == newY))
			{ // found goal
				return calcPath(nodes[oldX][oldY], current);
			}

			// for all adjacent nodes:
			List<T> adjacentNodes = getAdjacent(current);
			for(int i = 0; i < adjacentNodes.size(); i++)
			{
				T currentAdj = adjacentNodes.get(i);
				if(!openList.contains(currentAdj))
				{ // node is not in openList
					currentAdj.setPrevious(current); // set current node as
														// previous for this
														// node
					currentAdj.sethCosts(nodes[newX][newY]); // set h costs of
																// this node
																// (estimated
																// costs to
																// goal)
					currentAdj.setgCosts(current); // set g costs of this node
													// (costs from start to this
													// node)
					openList.add(currentAdj); // add node to openList
				}
				else
				{ // node is in openList
					if(currentAdj.getgCosts() > currentAdj.calculategCosts(current))
					{ // costs from current node are cheaper than previous costs
						currentAdj.setPrevious(current); // set current node as
															// previous for this
															// node
						currentAdj.setgCosts(current); // set g costs of this
														// node (costs from
														// start to this node)
					}
				}
			}
			if(openList.isEmpty())
			{ // no path exists
				return new LinkedList<>(); // return empty list
			}
		}
		return null; // unreachable
	}

	/**
	 * calculates the found path between two points according to
	 * their given <code>previousNode</code> field.
	 *
	 * @param start
	 * @param goal
	 * @return
	 */
	private List<T> calcPath(T start, T goal)
	{
		// TODO if invalid nodes are given (eg cannot find from
		// goal to start, this method will result in an infinite loop!)
		LinkedList<T> path = new LinkedList<>();

		T curr = goal;
		boolean done = false;
		while(!done)
		{
			path.addFirst(curr);
			curr = (T)curr.getPrevious();

			if(curr.equals(start))
			{
				done = true;
			}
		}
		return path;
	}

	/**
	 * returns the node with the lowest fCosts.
	 *
	 * @return
	 */
	private T lowestFInOpen()
	{
		// TODO currently, this is done by going through the whole openList!
		T cheapest = openList.get(0);
		for(int i = 0; i < openList.size(); i++)
		{
			if(openList.get(i).getfCosts() < cheapest.getfCosts())
			{
				cheapest = openList.get(i);
			}
		}
		return cheapest;
	}

	/**
	 * returns a LinkedList with nodes adjacent to the given node.
	 * if those exist, are walkable and are not already in the closedList!
	 */
	private List<T> getAdjacent(T node)
	{
		// TODO make loop
		int x = node.getxPosition();
		int y = node.getyPosition();
		List<T> adj = new LinkedList<>();

		T temp;
		if(x > 0)
		{
			temp = this.getNode((x - 1), y);
			if(temp.isWalkable() && !closedList.contains(temp))
			{
				temp.setIsDiagonaly(false);
				adj.add(temp);
			}
		}

		if(x < width)
		{
			temp = this.getNode((x + 1), y);
			if(temp.isWalkable() && !closedList.contains(temp))
			{
				temp.setIsDiagonaly(false);
				adj.add(temp);
			}
		}

		if(y > 0)
		{
			temp = this.getNode(x, (y - 1));
			if(temp.isWalkable() && !closedList.contains(temp))
			{
				temp.setIsDiagonaly(false);
				adj.add(temp);
			}
		}

		if(y < heigth)
		{
			temp = this.getNode(x, (y + 1));
			if(temp.isWalkable() && !closedList.contains(temp))
			{
				temp.setIsDiagonaly(false);
				adj.add(temp);
			}
		}

		// add nodes that are diagonaly adjacent too:
		if(CANMOVEDIAGONALY)
		{
			if((x < width) && (y < heigth))
			{
				temp = this.getNode((x + 1), (y + 1));
				if(temp.isWalkable() && !closedList.contains(temp))
				{
					temp.setIsDiagonaly(true);
					adj.add(temp);
				}
			}

			if((x > 0) && (y > 0))
			{
				temp = this.getNode((x - 1), (y - 1));
				if(temp.isWalkable() && !closedList.contains(temp))
				{
					temp.setIsDiagonaly(true);
					adj.add(temp);
				}
			}

			if((x > 0) && (y < heigth))
			{
				temp = this.getNode((x - 1), (y + 1));
				if(temp.isWalkable() && !closedList.contains(temp))
				{
					temp.setIsDiagonaly(true);
					adj.add(temp);
				}
			}

			if((x < width) && (y > 0))
			{
				temp = this.getNode((x + 1), (y - 1));
				if(temp.isWalkable() && !closedList.contains(temp))
				{
					temp.setIsDiagonaly(true);
					adj.add(temp);
				}
			}
		}
		return adj;
	}

	public <T> Vector2i getAdjacentNodeAround(int newX, int newY)
	{
		int i = 0, j = 0;
		boolean[][] nodesDone = new boolean[width][heigth];
		while(!done)
		{
			for(int a = newX - i; a < (newX + i + 1); a++)
			{
				for(int b = newY - j; b < (newY + j + 1); b++)
				{
					if((a < 0) || (a >= width) || (b < 0) || (b >= heigth))
					{
						continue;
					}
					if(nodesDone[a][b])
					{
						continue;
					}
					nodesDone[a][b] = true;
					if(nodes[a][b].isWalkable())
					{
						done = true;
						return new Vector2i(a, b);
					}
				}
			}
			i++;
			j++;
		}
		return new Vector2i(newX, newY);
	}

	@Deprecated
	public <T> Vector2i getAdjacentNodeAroundInFunctionOfMousePos(int newX, int newY)
	{
		int r = 0;
		double posX = Engine.inputs.getX() + Handler.getCamera().getOffset().x;
		double posY = Engine.inputs.getY() + Handler.getCamera().getOffset().y;
		boolean[][] nodesDone = new boolean[width][heigth];
		// test each tiles in a crescent radius
		// among this tiles quad get the lowest tile with the distance (in px)
		// between mouse pos and center of this tile
		// hashmap
		HashMap<Vector2i, Double> nodesDistances = new HashMap<>();
		do
		{
			nodesDistances.clear();
			for(int a = newX - r; a < (newX + r + 1); a++)
			{
				for(int b = newY - r; b < (newY + r + 1); b++)
				{
					if((a < 0) || (a >= width) || (b < 0) || (b >= heigth))
					{
						continue;
					}
					if(nodesDone[a][b])
					{
						continue;
					}
					nodesDone[a][b] = true;
					// System.out.println("pos " + newX + " " + newY);
					// System.out.println(a + " " + b);
					if(nodes[a][b].isWalkable())
					{
						// System.err.println(Math.sqrt(((nodes[a][b].getxPosition()
						// - posX) * (nodes[a][b].getxPosition() - posX)) +
						// ((nodes[a][b].getyPosition() - posY) *
						// (nodes[a][b].getyPosition() - posY))));
						nodesDistances.put(new Vector2i(a, b), Math.sqrt(((nodes[a][b].getxPosition() - posX) * (nodes[a][b].getxPosition() - posX)) + ((nodes[a][b].getyPosition() - posY) * (nodes[a][b].getyPosition() - posY))));
					}
				}
			}
			if(nodesDistances.isEmpty())
			{
				r++;
			}
			else
			{
				done = true;
			}
		}
		while(!done);
		Utils.sortByValue(nodesDistances);
		return nodesDistances.entrySet().iterator().next().getKey();
	}
}
