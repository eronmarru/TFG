package com.example.underworldstrack;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BandAdapter extends RecyclerView.Adapter<BandAdapter.ViewHolder> {

    private List<Band> bands;     // Lista de bandas que se mostrarán
    private Context context;      // Contexto para crear notificaciones

    public BandAdapter(Context context, List<Band> bands) {
        this.context = context;
        this.bands = bands;
    }

    // Clase que contiene las vistas de cada item de la lista
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgBand;              // Imagen de la banda
        TextView txtBandName, txtBandDescription;  // Nombre y descripción

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Conecta las vistas del layout item_band con las variables
            imgBand = itemView.findViewById(R.id.imgBand);
            txtBandName = itemView.findViewById(R.id.txtBandName);
            txtBandDescription = itemView.findViewById(R.id.txtBandDescription);
        }
    }

    // Crea una nueva vista para cada item (se reutilizan después)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout item_band.xml para cada elemento de la lista
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_band, parent, false);
        return new ViewHolder(view);
    }

    // Vincula los datos de una banda específica a las vistas del ViewHolder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Band band = bands.get(position);  // Obtiene la banda en la posición actual
        
        // Llena las vistas con los datos de la banda
        holder.imgBand.setImageResource(band.getImageResId());
        holder.txtBandName.setText(band.getName());
        holder.txtBandDescription.setText(band.getDescription());

        // Al tocar la imagen de la banda, muestra una notificación
        holder.imgBand.setOnClickListener(v -> {
            String title = band.getName();
            String text = "Esta banda es de la facción " + band.getDisplayFaction();

            // Crea intent que abre Bands activity al tocar la notificación
            Intent intent = new Intent(context, Bands.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            
            // PendingIntent para la acción de la notificación
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    context,
                    0,
                    intent,
                    Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                            ? PendingIntent.FLAG_IMMUTABLE      // Android 6+ requiere IMMUTABLE
                            : 0
            );

            // Construye la notificación
            NotificationCompat.Builder builder =
                    new NotificationCompat.Builder(context, Bands.CHANNEL_ID)
                            .setSmallIcon(R.drawable.icon)              // Icono pequeño
                            .setContentTitle(title)                     // Título (nombre banda)
                            .setContentText(text)                       // Texto (facción)
                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                            .setContentIntent(pendingIntent)            // Acción al tocar
                            .setAutoCancel(true);                       // Se quita sola al tocar

            NotificationManagerCompat manager = NotificationManagerCompat.from(context);
            int notificationId = position + 1;  // ID único por banda (1,2,3...)

            // Muestra notificación solo si Android <13 o tiene permiso POST_NOTIFICATIONS
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                    NotificationManagerCompat.from(context).areNotificationsEnabled()) {
                manager.notify(notificationId, builder.build());
            }
        });
    }

    // Devuelve el número total de bandas en la lista
    @Override
    public int getItemCount() {
        return bands.size();
    }
}
