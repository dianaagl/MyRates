package com.dianaagl.myrates;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.dianaagl.myrates.networking.CurrenciesList;
import com.dianaagl.myrates.networking.Currency;


public class MyAdapter extends BaseAdapter {
    int counter=60;
    private static CurrenciesStorage storage;
    InfoLoader myInfoLoader;
    public void Cancel(){
        myInfoLoader.cancel(true);
    }
    public MyAdapter(CurrenciesStorage storage){this.storage = storage;}
    @Override
    public int getCount() {
        if(storage.getLoadedList() != null)
        return storage.getLoadedList().getCurrencies().size();
        return counter;
    }

    @Override
    public Currency getItem(int position) {
        if(storage.getLoadedList() != null)
        return storage.getLoadedList().getCurrencies().get(position);
        else{
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        if(storage.getLoadedList() != null)
        return storage.getLoadedList().getCurrencies().get(position).hashCode();
        else{
            return -1;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null){
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) view.findViewById(R.id.Name);
            viewHolder.charcode = (TextView) view.findViewById(R.id.CharCode);
            viewHolder.code= (TextView) view.findViewById(R.id.NumCode);
            viewHolder.value = (TextView) view.findViewById(R.id.Value);
            viewHolder.nominal = (TextView) view.findViewById(R.id.nominal);

            view.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();

        myInfoLoader = new InfoLoader(holder, parent.getContext(), position,storage);
        myInfoLoader.execute();

        return view;
    }
    private static class ViewHolder{
        private TextView name;
        private TextView value;
        private TextView code;
        private TextView charcode;
        private TextView nominal;
    }



    private static class InfoLoader extends AsyncTask<Void, Void, InfoLoader.InfoBundle> {
        private WeakReference<ViewHolder> holder;
        private int position;
        private Context mContext;
        private int count;
        private CurrenciesStorage storage;
        public InfoLoader(ViewHolder holder, Context context, int position,CurrenciesStorage storage) {
            this.position = position;
            this.holder = new WeakReference<ViewHolder>(holder);
            mContext = context;
            this.storage = storage;

            //присваиваем ссылки на элементы холдера
        }

        @Override
        protected InfoBundle doInBackground(Void... params) {
            InfoBundle bundle = new InfoBundle();
            if (isCancelled()) return null;
            if(storage.isReady()){
                bundle.currenciesList =  storage.getLoadedList();
            }
            else {
                bundle.currenciesList = internetConnection();
                storage.setLoadedList(bundle.currenciesList);
            }
            if (bundle == null) {
                return null;
            }

            return bundle;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }

        @Override

        protected void onPostExecute(InfoBundle bundle) {
            if (bundle == null){
                return;
            }
            ViewHolder myHolder = holder.get();
            List<Currency> curlist = bundle.currenciesList.getCurrencies();


            count = curlist.size();
            if (position < count){
                Currency cur = curlist.get(position);
                myHolder.value.setText( String.valueOf(cur.getValue()));
                myHolder.name.setText(cur.getName());
                myHolder.charcode.setText(cur.getCharCode());
                myHolder.code.setText(String.valueOf(cur.getNumCode()));
                myHolder.nominal.setText(String.valueOf(cur.getNominal()));
            }
            onFinish();
        }

        public static class InfoBundle {
            CurrenciesList currenciesList;
        }
        private void onFinish(){

        }

        private CurrenciesList internetConnection()  {
            URL myURL = null;
            CurrenciesList currenciesListFromInternet = null;
            InputStream dataStream = null;
            try {
                myURL = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
                dataStream = myURL.openConnection().getInputStream();
                currenciesListFromInternet = CurrenciesList.readFromStream(dataStream);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                try {
                    dataStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return currenciesListFromInternet;
        }
    }
}


