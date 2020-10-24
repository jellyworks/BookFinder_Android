package com.womanup.bookfinder;

public abstract class ThreadTask<T1, T2> implements Runnable {
    T1 argument;
    T2 result;

    final public void execute(final T1 arg) {
        argument = arg;
        onPreExecute();
        Thread thread = new Thread(this);
        thread.start();

        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
            onPostExecute(null);
            return;
        }
        onPostExecute(result);
    }

    @Override
    public void run() {
        result = doInBackground(argument);
    }

    protected abstract void onPreExecute();

    protected abstract T2 doInBackground(T1 arg);

    protected abstract void onPostExecute(T2 result);
}
