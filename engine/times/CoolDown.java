package times;

public class CoolDown
{
	private long length, endTime;
	private boolean started = false;

	/**
	 * @param length the attack delay in ups
	 */
	public CoolDown(long length)
	{
		this.length = length;
		endTime = length;
	}

	public void update()
	{
		if(started)
		{
			endTime--;
		}
		if(endTime <= 0)
		{
			endTime = 0;
			started = false;
		}
	}

	public void restart()
	{
		endTime = length;
		started = true;
	}

	public void go()
	{
		started = true;
	}

	public void stop()
	{
		started = false;
	}

	public void terminate()
	{
		endTime = 0;
		started = false;
	}

	public boolean isActive()
	{
		return started;
	}

	public boolean isOver()
	{
		return endTime <= 0;
	}

	public long getTime()
	{
		return endTime;
	}

	public CoolDown setTimeAndRestart(int length)
	{
		this.length = length;
		restart();
		return this;
	}

	public void setTime(int length)
	{
		this.length = length;
	}
}
